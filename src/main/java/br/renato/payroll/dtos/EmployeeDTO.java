package br.renato.payroll.dtos;

import br.renato.payroll.entities.Employee;
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

	public EmployeeDTO(Long id, BigDecimal salary) {
		this.id = id;
		this.salary = salary;
	}
}
