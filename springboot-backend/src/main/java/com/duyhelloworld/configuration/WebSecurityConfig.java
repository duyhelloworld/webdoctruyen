package com.duyhelloworld.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.duyhelloworld.service.security.local.AppAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, jsr250Enabled = false, securedEnabled = false)
@AllArgsConstructor
public class WebSecurityConfig {
	private AppAuthenticationFilter appAuthenticationFilter;

	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	private AuthenticationConfiguration authConfiguration;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authConfiguration.getAuthenticationManager();
	}

    /**
	 * @param cors					  tắt cors mặc định vì đã cấu hình trong {@link CorsConfig}
     * @param CSRF                    tắt cái csrf mặc định vì đã dùng jwt rồi
     * @param sessionManagement       tắt session vì dùng jwt
     * @param appAuthenticationFilter filter check xác thực jwt
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				// .cors(c -> c.disable())
				// .csrf(c -> c.disable())
				.sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.formLogin(f -> f.failureHandler((rq, rp, ex) -> {
					throw ex;
				}))
				.oauth2Login(oauth -> 
					oauth
						.userInfoEndpoint(oauth2 -> oauth2.userService(oAuth2UserService)))
				.formLogin(Customizer.withDefaults())
				.addFilterBefore(appAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}