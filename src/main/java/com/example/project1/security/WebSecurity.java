package com.example.project1.security;

import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurity {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    JwtGeneratorService jwtGeneratorService;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**",
            "/swagger-ui.html", "/api/auth/**", "/api/test/**",
            "/authenticate", "/swagger.json/**", "/users/**",
            "/swagger-config", "/api-docs/swagger-config", "/api-docs"
    };

    private final JwtRequestFilter jwtRequestFilter;
    @Autowired
    public WebSecurity(@Lazy JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/users/forgetpassword").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/refreshToken").permitAll()
                        .requestMatchers("/users/doFindAllUsers").hasRole("ADMIN")
                        .requestMatchers("/users/user").hasRole("ADMIN")
                        .requestMatchers("/users/deleteUser/**").hasRole("ADMIN")
                        .requestMatchers("/users/doSaveUser").hasRole("ADMIN")
                        .requestMatchers("/users/updateUser").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/users/logout").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/NewBook/delete/**").hasRole("ADMIN")
                        .requestMatchers("/NewBook/book").permitAll()
                        .requestMatchers("/NewBook/doUpdateNewbook").hasRole("ADMIN")
                        .requestMatchers("/NewBook/doSaveNewbook").hasRole("ADMIN")
                        .anyRequest().authenticated())
                        .exceptionHandling(e -> e.accessDeniedPage("/denied")
                                 .authenticationEntryPoint(new AuthEntryPoint()));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}