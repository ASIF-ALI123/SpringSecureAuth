package com.footpath.store.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.footpath.store.filter.CustomJwtAuthenticationFilter;
import com.footpath.store.filter.JwtAuthenticationEntryPoint;

@Configuration
@EnableAsync
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private CustomOAuth2Userservice customOAuth2Userservice;

	@Autowired
	private CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
	
	
	@Configuration
	@Order(1)
	public class socialSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			System.out.println("socialSecurityConfigurerAdapter");

			http.csrf().disable().httpBasic().disable().formLogin().disable();


	        http.cors().and()
	        	.authorizeRequests().antMatchers("/oauth2/**", "/login**", "/auth/**").permitAll().anyRequest().authenticated().and()
	        	.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	        	.oauth2Login()
	        		.authorizationEndpoint().authorizationRequestRepository(new InMemoryRequestRepo())
	        		.and()
	        		.userInfoEndpoint()
	        		.userService(customOAuth2Userservice)
	        		.and()
					.successHandler(customOAuth2LoginSuccessHandler);
		}
		
		
		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration corsConfig = new CorsConfiguration();
			corsConfig.setAllowedMethods(Collections.singletonList("*"));
			corsConfig.setAllowedOrigins(Collections.singletonList("*"));
			corsConfig.setAllowedHeaders(Collections.singletonList("*"));
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", corsConfig);
			return source;
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/v3/api-docs/**", "/configuration/ui", "/swagger-resources/**",
					"/configuration/security", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/favicon.ico");
		}

	}
	
}
