package br.renato.payroll.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
@Getter
@Setter
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;
	private String name;
	private String cnpj;
	private String address;
	private String email;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	private List<Employee> employeeList;

	public Company(final String name, final String cnpj, final String address, final String email) {
		this.name = name;
		this.cnpj = cnpj;
		this.address = address;
		this.email = email;
	}
}
