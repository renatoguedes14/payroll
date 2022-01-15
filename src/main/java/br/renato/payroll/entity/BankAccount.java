package br.renato.payroll.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor
@Getter
@Setter
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private BigDecimal balance;
	@OneToOne
	@JoinColumn(name = "company")
	private Company company;
	@OneToOne
	@JoinColumn(name = "employee")
	private Employee employee;

	public BankAccount(BigDecimal balance, Company company) {
		this.balance = balance;
		this.company = company;
	}

	public BankAccount(BigDecimal balance, Employee employee) {
		this.balance = balance;
		this.employee = employee;
	}
}
