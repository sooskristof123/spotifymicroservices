package com.kristof.exp.ConfigService.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kristof.exp.ConfigService.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
@Lazy
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JWTVerifier jwtVerifier = null;
    private final UserService userService;
    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws JWTVerificationException, ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
                username = decodedJWT.getSubject();
            } catch (JWTVerificationException exception) {
                log.error("JWT token verification error: {}", exception.getMessage());
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
    public void setJwtVerifier(RSAPublicKey rsaPublicKey) {
        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey);
        jwtVerifier = JWT.require(algorithm).build();
    }
}
