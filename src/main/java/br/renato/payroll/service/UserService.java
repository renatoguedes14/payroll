package br.renato.payroll.service;

import br.renato.payroll.entity.User;
import br.renato.payroll.exception.DataIntegrityViolationException;
import br.renato.payroll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User createUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new DataIntegrityViolationException("User already registered. ");
		}
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		return userRepository.save(user);
	}
}
