package com.sample.sample.auth.filter;

import com.sample.sample.auth.CustomUserDetails;
import com.sample.sample.auth.CustomUserDetailsService;
import com.sample.sample.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.sample.sample.account.constants.AuthConstants.*;

@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtVerificationFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            Claims claims = verifyClaims(request);
            setAuthenticationToContext(claims);
        } catch(Exception e){
            log.info("claims 검증 안됨");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String accessToken = getAccessToken(request);

        return accessToken == null || !accessToken.startsWith(BEARER);
    }

    private Claims verifyClaims(HttpServletRequest request) {

        String accessToken = getAccessToken(request).replace(BEARER, "");

        return jwtProvider.getClaims(accessToken);
    }

    private String getAccessToken(HttpServletRequest request) {

        return request.getHeader(AUTHORIZATION);
    }

    private void setAuthenticationToContext(Claims claims) {

        CustomUserDetails principal =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(claims.getSubject());

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
