package br.renato.payroll.dto;

import br.renato.payroll.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {

	private Long id;
	private BigDecimal balance;
	private Long companyId;
	private Long employeeId;

	public BankAccountDTO(final BankAccount bankAccount) {
		this.id = bankAccount.getId();
		this.balance = bankAccount.getBalance();
		if (bankAccount.getCompany() != null) {
			this.companyId = bankAccount.getCompany().getId();
		} else {
			this.employeeId = bankAccount.getEmployee().getId();
		}
	}

	public BankAccountDTO(final Long id, final BigDecimal balance) {
		this.id = id;
		this.balance = balance;
	}
}
