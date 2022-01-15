package br.renato.payroll.dtos;

import br.renato.payroll.entities.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CompanyDTO {

	private Long id;
	private String name;
	private String cnpj;
	private String address;

	public CompanyDTO(final Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.cnpj = company.getCnpj();
		this.address = company.getAddress();
	}
}
