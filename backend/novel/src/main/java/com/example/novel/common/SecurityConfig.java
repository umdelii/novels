package com.example.novel.common;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.novel.member.filter.JWTCheckFilter;
import com.example.novel.member.handler.LoginFailureHandler;
import com.example.novel.member.handler.LoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // swagger 경로
                .requestMatchers("swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 이하 경로의 get매핑은 다 허용
                .requestMatchers(HttpMethod.GET, "/api/novels/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/members/register").permitAll()
                .anyRequest().authenticated());

        http.csrf(csrf -> csrf.disable());

        http.formLogin(login -> login
                .loginProcessingUrl("/api/members/login")
                .successHandler(loginSuccessHandler())
                .failureHandler(new LoginFailureHandler()));

        // security 들어오고서는 여기서 cors 설정해야함
        http.cors(httpSecurityCorsConfig -> httpSecurityCorsConfig.configurationSource(corsConfigurationSource()));

        // api 서버 상태
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 지정
        http.addFilterBefore(jwtCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(401);
                })
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(406);
                }));

        return http.build();
    }

    @Bean
    JWTCheckFilter jwtCheckFilter() {
        return new JWTCheckFilter();
    }

    // cors 설정변경
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        // token 生成のアルゴリズム
        RememberMeTokenAlgorithm eTokenAlgorithm = RememberMeTokenAlgorithm.SHA256;

        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("mykey", userDetailsService,
                eTokenAlgorithm);
        // 브라우저에서 넘어온 remember-me 쿠키 검증용 알고리즘
        services.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        services.setTokenValiditySeconds(60 * 60 * 24 * 7);
        return services;
    }

}
