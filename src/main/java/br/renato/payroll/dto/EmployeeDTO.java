package br.renato.payroll.dto;

import br.renato.payroll.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EmployeeDTO {

	private Long id;
	@NotBlank(message = "Name mustn't be empty. ")
	private String name;
	@NotBlank(message = "CPF mustn't be empty. ")
	private String cpf;
	@NotBlank(message = "Address mustn't be empty. ")
	private String address;
	@NotNull(message = "Company id mustn't be null. ")
	private Long companyId;
	@NotNull(message = "Salary mustn't be null. ")
	private BigDecimal salary;

	public EmployeeDTO(final Employee employee) {
		this.id = employee.getId();
		this.name = employee.getName();
		this.cpf = employee.getCpf();
		this.address = employee.getAddress();
		this.companyId = employee.getCompany().getId();
		this.salary = employee.getSalary();
	}

	public EmployeeDTO(final Long id, final String name, final BigDecimal salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
}
