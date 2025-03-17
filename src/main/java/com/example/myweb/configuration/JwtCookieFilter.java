// package com.example.myweb.configuration;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import com.example.myweb.dto.request.IntrospectRequest;
// import com.example.myweb.service.AuthenticationService;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class JwtCookieFilter extends OncePerRequestFilter {

// private final JwtDecoder jwtDecoder;
// private final AuthenticationService authenticationService;

// @Override
// protected void doFilterInternal(HttpServletRequest request,
// HttpServletResponse response, FilterChain filterChain)
// throws ServletException, IOException {
// String token = authenticationService.getTokenFromCookie(request);

// if (token != null) {
// try {
// // Kiểm tra token có bị blacklist không
// boolean isBlacklisted = authenticationService.introspect(
// IntrospectRequest.builder().token(token).build());

// if (!isBlacklisted) {
// SecurityContextHolder.clearContext();
// filterChain.doFilter(request, response);
// return;
// }

// // Giải mã token
// Jwt jwt = jwtDecoder.decode(token);

// String username = jwt.getSubject();

// String scope = jwt.getClaimAsString("scope");
// List<SimpleGrantedAuthority> authorities = (scope != null &&
// !scope.isBlank())
// ? Arrays.stream(scope.trim().split("\\s+"))
// .map(SimpleGrantedAuthority::new)
// .toList()
// : Collections.emptyList();
// // Xác thực người dùng
// UsernamePasswordAuthenticationToken authentication = new
// UsernamePasswordAuthenticationToken(
// username, null, authorities);
// SecurityContextHolder.getContext().setAuthentication(authentication);
// } catch (Exception e) {
// SecurityContextHolder.clearContext();
// }
// }
// filterChain.doFilter(request, response);
// }
// }
// gửi token qua cookie