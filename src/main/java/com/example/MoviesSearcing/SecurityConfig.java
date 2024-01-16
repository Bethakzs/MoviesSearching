package com.example.MoviesSearcing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, InitialAuthenticationFilter initialAuthenticationFilter) throws Exception {
//
//        http.addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class);
//
//        http.authorizeHttpRequests(authz -> authz
//                .requestMatchers("/h2-console/**").permitAll()
//                .anyRequest().authenticated());
//
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
}
