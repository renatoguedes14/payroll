package br.renato.payroll.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDTO {

	@NotNull
	@Setter(AccessLevel.NONE)
	private Long companyId;
	@NotNull
	private String cpf;
	@NotNull
	@DecimalMin(value = "0.0")
	private BigDecimal amount;
}
