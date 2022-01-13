package br.renato.payroll.services;

import br.renato.payroll.entities.CompanyUser;
import br.renato.payroll.entities.ConfirmationToken;
import br.renato.payroll.repository.CompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyUserService implements UserDetailsService {

	private final static String USER_NOT_FOUND_MSG = "User with email %s not found. ";
	private final static String EMAIL_TAKEN_MSG = "Email %s already taken. ";

	private final CompanyUserRepository companyUserRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return companyUserRepository.findByEmail(username).orElseThrow(() ->
				new UsernameNotFoundException(USER_NOT_FOUND_MSG));
	}

	public String signUpUser(CompanyUser companyUser) {
		boolean userExists = companyUserRepository.findByEmail(companyUser.getEmail()).isPresent();

		if (userExists) {
			throw new IllegalStateException(EMAIL_TAKEN_MSG);
		}

		String encodedPassword = passwordEncoder.encode(companyUser.getPassword());
		companyUser.setPassword(encodedPassword);
		companyUserRepository.save(companyUser);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				companyUser
		);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		return token;
	}

	public void enableCompanyUser(String email) {
		companyUserRepository.enableCompanyUser(email);
	}
}
