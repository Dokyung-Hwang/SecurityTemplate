package com.sample.sample.auth;


import java.util.List;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.sample.sample.account.constants.AuthConstants.*;

@Component
public class Customizers {

    public static Customizer<CorsConfigurer<HttpSecurity>> getCors() {
        return cors -> {
            CorsConfiguration configuration = new CorsConfiguration();

            // Request
            configuration.setAllowedOrigins(List.of("https://growstory.vercel.app/", "http://localhost:3000"));
            configuration.addAllowedMethod("*");
            configuration.addAllowedHeader("*");
//            configuration.setAllowedHeaders(List.of("Authorization", "Refresh"));     // 실제 서비스에서는 이런 식으로 직접 지정해 줘야됨.
            configuration.setAllowCredentials(true);        // 요청에 인증 정보를 포함할 수 있는 지

            // Response
            configuration.setExposedHeaders(List.of(
                    AUTHORIZATION, REFRESH, LOCATION));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            cors.configurationSource(source);
        };
    }
}
