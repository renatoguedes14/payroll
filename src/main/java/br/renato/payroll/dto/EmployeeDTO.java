package br.renato.payroll.dto;

import br.renato.payroll.entity.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EmployeeDTO {

	private Long id;
	private String name;
	private String cpf;
	private String address;
	private Long companyId;
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
