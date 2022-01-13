package br.renato.payroll.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.swagger2.mappers.CompatibilityModelMapper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Table(name = "employees")
@Getter
@Setter
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;

	private String name;
	private String cpf;
	private String address;
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(name = "company", referencedColumnName = "id")
	private Company company;

	public Employee(String name, String cpf, String address, BigDecimal balance, Company company) {
		this.name = name;
		this.cpf = cpf;
		this.address = address;
		this.balance = balance;
		this.company = company;
	}
}
