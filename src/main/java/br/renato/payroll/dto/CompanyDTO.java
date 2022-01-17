package br.renato.payroll.dto;

import br.renato.payroll.entity.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CompanyDTO {

	private Long id;
	@NotBlank(message = "Name mustn't be empty. ")
	private String name;
	@NotBlank(message = "CNPJ mustn't be empty. ")
	private String cnpj;
	@NotBlank(message = "Address mustn't be empty. ")
	private String address;
	@Email(message = "E-mail must be valid. ")
	@NotBlank(message = "E-mail mustn't be empty. ")
	private String email;

	public CompanyDTO(final Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.cnpj = company.getCnpj();
		this.address = company.getAddress();
		this.email = company.getEmail();
	}
}
