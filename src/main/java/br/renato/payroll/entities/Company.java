package br.renato.payroll.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "companies")
@Getter
@Setter
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;

	private String name;
	private String cnpj;
	private String address;
	private BigDecimal balance;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	private List<Employee> employeeList;

	public Company(String name, String cnpj, String address, BigDecimal balance) {
		this.name = name;
		this.cnpj = cnpj;
		this.address = address;
		this.balance = balance;
	}
}
