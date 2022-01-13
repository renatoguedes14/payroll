package br.renato.payroll.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ValidationError extends ErrorDTO {

	private static final long serialVersionUID = 1L;

	private final List<FieldMessage> errorList = new ArrayList<>();

	public ValidationError(Long timestamp, Integer status, String error) {
		super(timestamp, status, error);
	}

	public void addError(String fieldName, String message) {
		this.errorList.add(new FieldMessage(fieldName, message));
	}
}
