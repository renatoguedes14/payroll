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

}
