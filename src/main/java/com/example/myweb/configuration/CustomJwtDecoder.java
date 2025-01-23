package com.example.myweb.configuration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.myweb.dto.request.IntrospectRequest;
import com.example.myweb.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;

import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {

    AuthenticationService authenticationService;
    NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (Boolean.FALSE
                    .equals((authenticationService.introspect(IntrospectRequest.builder().token(token).build())))) {
                throw new JwtException("Token invalid");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }
        return nimbusJwtDecoder.decode(token);
    }

}
