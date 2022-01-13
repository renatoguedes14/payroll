package br.renato.payroll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<ErrorDTO> objectNotFoundException(ObjectNotFoundException e) {
		ErrorDTO errorDTO = new ErrorDTO(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
	}

	public ResponseEntity<ErrorDTO> dataIntegrityViolationException(DataIntegrityViolationException e) {
		ErrorDTO errorDTO = new ErrorDTO(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
	}

	public ResponseEntity<ErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		ValidationError error = new ValidationError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Validation error. ");

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
