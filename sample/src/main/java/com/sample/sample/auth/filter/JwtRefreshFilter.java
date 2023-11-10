package com.sample.sample.auth.filter;

import com.sample.sample.auth.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sample.sample.account.constants.AuthConstants.*;

public class JwtRefreshFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtRefreshFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        if(!request.getMethod().equals("POST")){
            throw new RuntimeException();
        }
        else{
            try {
                String refreshToken = getRefreshToken(request);

                jwtProvider.validateToken(refreshToken);

                String refilledAccessToken =
                        jwtProvider.refillAccessToken(refreshToken, ACCESS_TOKEN_EXPIRE_TIME);

                response.setHeader(AUTHORIZATION, BEARER + refilledAccessToken);
            } catch (Exception exception) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "알 수 없는 오류입니다. 다시 시도해주세요.");
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return !request.getRequestURI().equals(REFRESH_URL);
    }

    private String getRefreshToken(HttpServletRequest request) {

        String refreshToken = request.getHeader(REFRESH);

        if (refreshToken == null) {
            throw new JwtException("e".repeat(50));
        }

        return refreshToken.replace(BEARER, "");
    }
}
