package br.renato.payroll.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenAuthenticationService {

	private static final String key = "MySecret";

	private static final long expiresAt = 3600000;

	private static final String type = "Bearer";
	private static final String header = "Authorization";

	public static void addAuthentication(HttpServletResponse response, String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiresAt))
				.signWith(SignatureAlgorithm.HS512, key).compact();

		response.addHeader(header, type + " " + JWT);
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(header);

		if (token != null) {
			String user = Jwts.parser().setSigningKey(key).parseClaimsJws(token.replace(type, "")).getBody()
					.getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		return null;
	}
}
