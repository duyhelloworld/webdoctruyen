package ptit.edu.vn.configuration;

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

import ptit.edu.vn.service.security.AppAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	private AppAuthenticationFilter appAuthenticationFilter;

	private UserDetailsService userDetailsService;

	public WebSecurityConfig(AppAuthenticationFilter appAuthenticationFilter, UserDetailsService userDetailsService) {
		this.appAuthenticationFilter = appAuthenticationFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    /**
     * @param CSRF                    tắt cái csrf mặc định vì đã dùng jwt rồi
     * @param authorizeHttpRequest 	  phân quyền request
     * @param sessionManagement       t?t session vì dùng jwt
     * @param appAuthenticationFilter filter check xác thực jwt
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(c -> c.disable())
				.csrf(c -> c.disable())
				.userDetailsService(userDetailsService)
				.sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.addFilterBefore(appAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}