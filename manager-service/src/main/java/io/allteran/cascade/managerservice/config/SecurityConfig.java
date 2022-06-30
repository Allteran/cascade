package io.allteran.cascade.managerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig  {
    private final String[] BASIC_ROLES={"EMPLOYEE", "ENGINEER", "HEAD_ENGINEER", "MANAGER", "ADMIN", "DIRECTOR"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().hasAnyRole(BASIC_ROLES);
        return httpSecurity.build();
    }
}
