package br.renato.payroll.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Table(name = "employees")
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;
	private String name;
	private String cpf;
	private String address;
	private BigDecimal salary;
	@ManyToOne
	@JoinColumn(name = "company", referencedColumnName = "id")
	private Company company;

	public Employee(String name, String cpf, String address, BigDecimal salary, Company company) {
		this.name = name;
		this.cpf = cpf;
		this.address = address;
		this.salary = salary;
		this.company = company;
	}
}
