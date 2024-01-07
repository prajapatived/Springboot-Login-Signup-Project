package com.main.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService getDetailService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	public static final String[] ENDPOINTS_WHITELIST = {
            "/css/**",
            "/",
            "/register",
            "/js/**",
            "/img/**",
            "/error/**",
            "/signin",
            "/saveUser"
//            "/about/**",
//            "/user/home**",
           
    };
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(request ->
                request.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/signin")
                        .loginProcessingUrl("/userLogin")
                        .failureUrl("/signin?error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/user/profile")
                        .permitAll());
        return http.build();
    }
}
