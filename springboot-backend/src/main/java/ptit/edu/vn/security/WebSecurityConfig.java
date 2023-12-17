package ptit.edu.vn.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
	@Autowired
	private AppAuthenticationFilter appAuthenticationFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * @param CSRF                    tắt cái csrf mặc định vì đã dùng jwt rồi
	 * @param authorizeHttpRequests   cho phép tất cả các request đến /auth/**
	 *                                mà không cần xác thực (để vào api
	 *                                signup/signin)
	 *                                các request khác thì phải xác thực
	 * @param sessionManagement       tắt session vì dùng jwt
	 * @param authenticationProvider  cung cấp khả năng mở rộng cho google,
	 *                                facebook, ...
	 * @param appAuthenticationFilter filter để xác thực jwt (tự định nghĩa)
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(c -> c.disable())
				.authorizeHttpRequests(request -> {
					request
						.anyRequest().permitAll();
					})
				.exceptionHandling(E -> E.disable())
				.sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(appAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}