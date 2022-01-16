package br.renato.payroll.service;

import br.renato.payroll.entity.User;
import br.renato.payroll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user != null) {
			return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
					.password(user.getPassword()).roles("CLIENT").build();
		} else {
			throw new UsernameNotFoundException("User not found. ");
		}
	}
}
