package br.renato.payroll.services;

import br.renato.payroll.entities.Company;
import br.renato.payroll.entities.Employee;
import br.renato.payroll.exceptions.ObjectNotFoundException;
import br.renato.payroll.repositories.CompanyRepository;
import br.renato.payroll.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

	private final CompanyRepository companyRepository;
	private final EmployeeRepository employeeRepository;

	public void withdrawal(final Long id, final BigDecimal amount) {
		Optional<Company> companyOptional = companyRepository.findById(id);
		Company company = companyOptional.orElseThrow(() -> new ObjectNotFoundException("Company not found. "));
		company.setBalance(companyOptional.get().getBalance());

		if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(company.getBalance()) <= 0) {
			company.setBalance(company.getBalance().subtract(amount));
		}
	}

	public void deposit(final Long id, final BigDecimal amount) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		Employee employee = employeeOptional.orElseThrow(() -> new ObjectNotFoundException("Employee not found. "));
		employee.setBalance(employeeOptional.get().getBalance());

		if (amount.compareTo(BigDecimal.ZERO) > 0) {
			employee.setBalance(employee.getBalance().add(amount));
		}
	}
}
