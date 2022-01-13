package br.renato.payroll.resources;

import br.renato.payroll.dtos.CompanyUserDTO;
import br.renato.payroll.services.RegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "/registration", tags = "Authentication requests. ")
@RestController
@RequestMapping(path = "/registration")
@RequiredArgsConstructor
public class RegistrationResource {

	private final RegistrationService registrationService;

	@ApiOperation(value = "Performs user's registration. ", response = String.class)
	@PostMapping
	public ResponseEntity<String> register(@Valid @RequestBody CompanyUserDTO companyUserDTO) {
		String token = registrationService.register(companyUserDTO);

		return ResponseEntity.ok().body(token);
	}

	@ApiOperation(value = "Confirms the generated token when registered. ", response = String.class)
	@GetMapping(path = "confirm")
	public ResponseEntity<String> confirm(@RequestParam("token") String token) {
		String response = registrationService.confirmToken(token);

		return ResponseEntity.ok().body(response);
	}
}
