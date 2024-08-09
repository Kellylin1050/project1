package com.example.project1.security;

import com.example.project1.Dao.UserRepository;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
//@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
//@RequiredArgsConstructor
public class WebSecurity {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    JwtGeneratorService jwtGeneratorService;

  /*  @GetMapping("/")
    public String method(@CurrentSecurityContext SecurityContext context) {
        return context.getAuthentication().getName();
    }*/


    /*private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
            "/api/test/**", "/authenticate" ,"/swagger.json/**","users/**"};*/
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
                        .requestMatchers("/users/forgetpassword").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/doFindById/**").hasRole("ADMIN")
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
                                 .authenticationEntryPoint(new Http403ForbiddenEntryPoint()));
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



    /*@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder().username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder().username("admin")
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }*/

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 在內存中創建了一個用戶，該用戶的名稱為user，密碼為password，用戶角色為USER。
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER");
    }*/

    /*
     @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER_ROLE")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager () {
        String  generatedPassword  ="123456";
        return  new  InMemoryUserDetailsManager (User.withUsername( "user" )
                .password( generatedPassword).roles( "ROLE_USER" ).build());
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher (ApplicationEventPublisher delegate) {
        return  new  DefaultAuthenticationEventPublisher (delegate);
    }

    @Bean
    protected void cconfigure(HttpSecurity http) throws Exception{
        http
                .logout()
                .logoutUrl("/my/logout")
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler()
                .invalidateHttpSession(true)
                .addLogoutHandler()
                .deleteCookies()
                .and()
   */

    /* private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/v2/API-docs",
            "/v3/API-docs",
            "/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.index.html"
    };
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers(AUTH_WHITELIST).permitAll();
                            authorizeHttp.anyRequest().authenticated();
                        });
        return http.build();
    }
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/index.html/");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
                .csrf(Csrf -> Csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                              .requestMatchers("/login").anonymous()
                                .requestMatchers("/users/register").permitAll()
                                .requestMatchers("/users/login").permitAll()
                                .requestMatchers("/csrf").permitAll()
                                .requestMatchers("/users/forgetpassword").permitAll()
                                .requestMatchers("/users/user").hasRole("ADMIN")
                                .requestMatchers("/NewBook/**").permitAll()
                                //.requestMatchers("/doUpdateNewbook").hasRole("ADMIN")
                                //.requestMatchers("/users/updateUser").hasRole("ROLE_USER")
                                //.requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest()
                                .authenticated()
                )
                //.formLogin(Customizer.withDefaults())
                .logout(withDefaults());


        return http.build();*/
           /*http.addFilterBefore(new AuditInterceptor() , BasicAuthenticationFilter.class);
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access((authentication, object) ->{
                            if(authentication.get() instanceof AnonymousAuthenticationToken)
                                return new AuthorizationDecision(false);

                            return new AuthorizationDecision(true);
                        })
                );
        return http.build();
    }

    private static final String[] AUTH_WHITELIST = {

            // for Swagger UI v2
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",

            // for Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };*/

    }

