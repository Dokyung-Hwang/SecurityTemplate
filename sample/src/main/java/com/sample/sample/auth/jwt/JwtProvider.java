package com.sample.sample.auth.jwt;


import com.sample.sample.auth.CustomUserDetails;
import com.sample.sample.auth.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import static com.sample.sample.account.constants.AuthConstants.CLAIM_AUTHORITY;
import static com.sample.sample.account.constants.AuthConstants.CLAIM_ID;

@Component
public class JwtProvider {

//    @Value("${}")
    private String key = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKXROG4n3srR2asr1BIhvcNAQEBBQADSwAwSAJBAKXROG4ROG4n3srR2asr1BIhvcNAQ";

    private Key secretKey;

    private CustomUserDetailsService userDetailsService;


    public JwtProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    // create access token
    public String createAccessToken(Authentication authentication, Long tokenExpireTime) {

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(CLAIM_AUTHORITY, getAuthorities(authentication))
                .setExpiration(getExpiration(tokenExpireTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // create refresh token
    public String createRefreshToken(Authentication authentication, long tokenExpireTime){

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(getExpiration(tokenExpireTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String refillAccessToken(String refreshToken, long tokenExpireTime) {
        // verified token
        Claims claims = getClaims(refreshToken);

        String username = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return createAccessToken(authentication, tokenExpireTime);
    }


    private String getAuthorities(Authentication authentication) {

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Date getExpiration(Long tokenExpireTime) {
        return new Date(new Date().getTime() + tokenExpireTime);
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException();
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
