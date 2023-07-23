package com.przemekbarczyk.springlibraryrestapi.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.przemekbarczyk.springlibraryrestapi.security.UserPrincipal;
import com.przemekbarczyk.springlibraryrestapi.security.UserPrincipalAuthenticationToken;
import com.przemekbarczyk.springlibraryrestapi.utility.JWTTokenUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String HEADER_STRING = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JWTTokenUtility jwtTokenUtility;

    @Override protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(token);

        if (decodedJWT != null) {

            String role = decodedJWT.getClaim("role").asString();

            UserPrincipal userPrincipal = new UserPrincipal(
                    Long.parseLong(decodedJWT.getSubject()),
                    decodedJWT.getClaim("email").asString(),
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );

            UserPrincipalAuthenticationToken authentication = new UserPrincipalAuthenticationToken(userPrincipal);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
