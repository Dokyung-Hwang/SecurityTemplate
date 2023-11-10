package com.sample.sample.config;

import com.sample.sample.auth.filter.JwtAuthenticationFilter;
import com.sample.sample.auth.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

import static com.sample.sample.account.constants.AuthConstants.LOGIN_URL;



@Component
public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {

    private final JwtProvider jwtProvider;

    public CustomFilterConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider, authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);

        // jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
        // jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

//        JwtRefreshFilter jwtRefreshFilter = new JwtRefreshFilter(jwtProvider);
//        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtProvider);

        builder
                .addFilter(jwtAuthenticationFilter);
//                .addFilterAfter(jwtRefreshFilter, JwtAuthenticationFilter.class)
//                .addFilterAfter(jwtVerificationFilter, JwtRefreshFilter.class);
    }
}
