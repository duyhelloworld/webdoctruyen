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
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

    /**
     * @param CSRF                    tắt cái csrf mặc định vì đã dùng jwt rồi
     * @param authorizeHttpRequest 	  phân quyền request
     * @param sessionManagement       t?t session vì dùng jwt
     * @param authenticationProvider  mở rộng cho google, facebook, ...
     * @param appAuthenticationFilter filter check xác thực jwt
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(c -> c.disable())
				.csrf(c -> c.disable())
				.authorizeHttpRequests(request -> {
					request
						.requestMatchers("/api/auth/**").permitAll()
						.anyRequest().authenticated();
					})
				.sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(appAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}