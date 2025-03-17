package com.example.myweb.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.AuthenticationRequest;
import com.example.myweb.dto.request.IntrospectRequest;
import com.example.myweb.dto.respone.AuthenticationRespone;
import com.example.myweb.entity.InvalidatedToken;
import com.example.myweb.entity.Role;
import com.example.myweb.entity.User;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.mapper.UserMapper;
import com.example.myweb.repository.InvalidatedTokenRepository;
import com.example.myweb.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    UserMapper userMapper;

    @NonFinal
    @Value("${signer.key}")
    String key;

    @NonFinal
    @Value("${valid.duration}")
    long validDuration;
    @NonFinal
    @Value("${refreshable.duration}")
    long refreshableDuration;

    public Boolean introspect(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return !invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
    }

    public AuthenticationRespone refreshToken(IntrospectRequest request) throws ParseException, JOSEException {

        var signToken = veriFier(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signToken.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return AuthenticationRespone.builder().token(generateToken(user)).build();
    }

    public SignedJWT veriFier(String token) throws JOSEException, ParseException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiration = Date.from(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                    .plus(refreshableDuration, ChronoUnit.SECONDS));

            if (expiration.before(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            // Xác minh chữ ký
            JWSVerifier verifier = new MACVerifier(key.getBytes());
            if (!signedJWT.verify(verifier)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

    }

    public AuthenticationRespone login(AuthenticationRequest request) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationRespone.builder().user(userMapper.toUserRespone(user)).token(generateToken(user)).build();

    }

    public void logout(String token) throws JOSEException, ParseException {

        var signToken = veriFier(token);

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);

            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

    }

    private String buildScope(User user) {
        if (user == null || user.getRole() == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        Role role = user.getRole();

        stringJoiner.add("ROLE_" + role.getName());

        if (!CollectionUtils.isEmpty(role.getPermissions())) {
            role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        }
        return stringJoiner.toString();
    }

}
