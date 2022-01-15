package br.renato.payroll.dto;

import br.renato.payroll.entity.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

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
