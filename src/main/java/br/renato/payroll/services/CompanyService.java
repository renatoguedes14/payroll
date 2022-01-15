package br.renato.payroll.services;

import br.renato.payroll.dtos.CompanyDTO;
import br.renato.payroll.dtos.TransactionDTO;
import br.renato.payroll.entities.Company;
import br.renato.payroll.entities.Employee;
import br.renato.payroll.exceptions.DataIntegrityViolationException;
import br.renato.payroll.exceptions.ObjectNotFoundException;
import br.renato.payroll.repositories.CompanyRepository;
import br.renato.payroll.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final EmployeeRepository employeeRepository;
	private final TransactionService transactionService;

	public Company findCompany(final Long id) {
		Optional<Company> company = companyRepository.findById(id);

		return company.orElseThrow(() -> new ObjectNotFoundException(
				"Company not found. "));
	}

	public List<Company> findAllCompanies() {
		return companyRepository.findAll();
	}

	public Company createCompany(final CompanyDTO companyDTO) {
		if (findByCnpj(companyDTO) != null) {
			throw new DataIntegrityViolationException("Company already registered in database. ");
		}
		return companyRepository.save(new Company(companyDTO.getName(), companyDTO.getCnpj(), companyDTO.getAddress(),
				companyDTO.getBalance()));
	}

	public Company updateCompany(final Long id, @Valid final CompanyDTO companyDTO) {
		Company company = findCompany(id);

		if (findByCnpj(companyDTO) != null && !Objects.equals(findByCnpj(companyDTO).getId(), id)) {
			throw new DataIntegrityViolationException("CNPJ is already registered in database as another company's CNPJ. ");
		}
		company.setName(companyDTO.getName());
		company.setCnpj(companyDTO.getCnpj());
		company.setAddress(companyDTO.getAddress());
		company.setBalance(companyDTO.getBalance());
		return companyRepository.save(company);
	}

	public void deleteCompany(final Long id) {
		Company company = findCompany(id);

		if (company.getEmployeeList().size() > 0) {
			throw new DataIntegrityViolationException("Company has one or more employees attached to it. ");
		}
		companyRepository.delete(company);
	}

	public Company checkBalance(final Long id) {
		Optional<Company> companyOptional = companyRepository.findById(id);
		if (companyOptional.isPresent()) {
			return companyOptional.get();
		}
		throw new ObjectNotFoundException("Company not found. ");
	}

	public void transfer(final TransactionDTO transactionDTO) {
		Optional<Company> companyOptional = companyRepository.findById(transactionDTO.getCompanyId());
		Optional<Employee> employeeOptional = employeeRepository.getByCpf(transactionDTO.getCpf());

		if (companyOptional.isEmpty()) {
			throw new ObjectNotFoundException("Company not found. ");
		}

		if (employeeOptional.isEmpty()) {
			throw new ObjectNotFoundException("Employee not found. ");
		}

		Company company = companyOptional.get();
		Employee employee = employeeOptional.get();

		if (transactionDTO.getAmount().compareTo(company.getBalance()) > 0) {
			throw new DataIntegrityViolationException("Insufficient balance");
		}

		transactionService.withdrawal(company.getId(), transactionDTO.getAmount());
		transactionService.deposit(employee.getId(), transactionDTO.getAmount());

		companyRepository.save(company);
		employeeRepository.save(employee);
	}


	private Company findByCnpj(final CompanyDTO companyDTO) {
		return companyRepository.findByCnpj(companyDTO.getCnpj());
	}
}
