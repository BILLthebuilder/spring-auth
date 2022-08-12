package com.spring.auth.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Repository;

@Configuration
@Slf4j
@Repository
public class SecurityConfig {
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests((authz) -> {
                try {
                    authz
                            .and().authorizeHttpRequests()
                            .antMatchers("/actuator/**")
                            .permitAll()
                            .antMatchers("/api/auth/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                } catch (Exception e) {
                    log.error("Filter Chain error={}",e);
                }
            });
    return http.build();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
