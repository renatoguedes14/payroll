package br.renato.payroll.security;

import br.renato.payroll.entity.User;
import br.renato.payroll.service.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JwtLoginFilter(String url, AuthenticationManager manager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(manager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						user.getPassword(),
						Collections.emptyList()
				)
		);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                        FilterChain filterChain, Authentication authentication) {
		TokenAuthenticationService.addAuthentication(response, authentication.getName());
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                          AuthenticationException failed) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Failed authentication, incorrect username or password");
	}
}
