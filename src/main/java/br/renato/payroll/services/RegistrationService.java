package br.renato.payroll.services;

import br.renato.payroll.dtos.CompanyUserDTO;
import br.renato.payroll.entities.CompanyUser;
import br.renato.payroll.entities.ConfirmationToken;
import br.renato.payroll.entities.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {

	private final CompanyUserService companyUserService;
	private final ConfirmationTokenService confirmationTokenService;

	public String register(CompanyUserDTO companyUserDTO) {
		return companyUserService.signUpUser(new CompanyUser(
				companyUserDTO.getFirstName(),
				companyUserDTO.getLastName(),
				companyUserDTO.getEmail(),
				companyUserDTO.getPassword(),
				UserRole.USER
		));

	}

	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
				new IllegalStateException("Token not found. "));

		if (confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("Email already confirmed. ");
		}
		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Token expired. ");
		}
		confirmationTokenService.setConfirmedAt(token);
		companyUserService.enableCompanyUser(confirmationToken.getCompanyUser().getEmail());
		return "Confirmed. ";
	}
}
