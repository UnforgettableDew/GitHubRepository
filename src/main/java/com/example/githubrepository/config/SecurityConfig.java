package com.example.githubrepository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
//                .exceptionHandling().accessDeniedHandler(new ApplicationAccessDeniedHandler())
//                .authenticationEntryPoint(new ApplicationAuthenticationEntryPoint())
//                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**", "/greeting","/github/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/github/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                    .authenticationProvider(authenticationProvider())
//                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login();
//                .clientRegistrationRepository(clientRegistrationRepository())
//                .authorizedClientRepository(authorizedClientRepository())
//                        .and().successHandler(successHandler());
        httpSecurity.cors().disable();
        return httpSecurity.build();
    }
}