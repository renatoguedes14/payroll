package br.renato.payroll.resource;

import br.renato.payroll.entity.User;
import br.renato.payroll.exception.DataIntegrityViolationException;
import br.renato.payroll.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/users", tags = "Allows you to create users to login. ")
@RestController
@RequestMapping(value = "/auth/users/create")
@RequiredArgsConstructor
public class UserResource {

	private final UserService userService;

	@ApiOperation(value = "Create user. ")
	@PostMapping
	public ResponseEntity<User> create(@RequestBody final User user) {
		try {
			return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
	}
}
