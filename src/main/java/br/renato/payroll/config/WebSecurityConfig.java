package br.renato.payroll.config;

import br.renato.payroll.security.JwtAuthenticationFilter;
import br.renato.payroll.security.JwtLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth/users/create").permitAll()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers("/swagger-ui/index.html/").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JwtLoginFilter("/login", authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
