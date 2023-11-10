package com.sample.sample.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample.auth.dto.JwtLogin;
import com.sample.sample.auth.jwt.JwtProvider;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sample.sample.account.constants.AuthConstants.*;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, AuthenticationManager authenticationManager1) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager1;
    }

    // 인증 시도 필터(로그인 필터) ?
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new RuntimeException();
        }

        try {
            JwtLogin loginDto = getLoginDtoFrom(request);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);

        } catch (BadCredentialsException e) {
            return null;
            // 잘못된 자격 증명 처리
        } catch (LockedException e) {
            return null;
            // 계정 잠금 처리
        } catch (DisabledException e) {
            return null;
            // 계정 비활성화 처리
        } catch (AccountExpiredException e) {
            return null;
            // 계정 만료 처리
        } catch (CredentialsExpiredException e) {
            return null;
            // 자격 증명(비밀번호) 만료 처리
        } catch (UsernameNotFoundException e) {
            return null;
            // 사용자가 존재하지 않는 경우 처리
        } catch (AuthenticationServiceException e) {
            return null;
            // 다른 인증 예외 처리
        } catch (Exception e) {
            return null;
        }
    }

    private JwtLogin getLoginDtoFrom(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), JwtLogin.class);
    }


    // 로그인 성공 시 뭐할 지
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtProvider.createAccessToken(authResult, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtProvider.createRefreshToken(authResult, REFRESH_TOKEN_EXPIRE_TIME);

        response.setHeader(AUTHORIZATION, BEARER + accessToken);
        response.setHeader(REFRESH, BEARER + refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("로그인 성공");
    }
}
