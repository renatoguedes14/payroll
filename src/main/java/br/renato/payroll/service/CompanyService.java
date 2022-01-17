package br.renato.payroll.service;

import br.renato.payroll.dto.BankAccountDTO;
import br.renato.payroll.dto.CompanyDTO;
import br.renato.payroll.dto.EmailDTO;
import br.renato.payroll.dto.EmployeeDTO;
import br.renato.payroll.entity.Company;
import br.renato.payroll.exception.DataIntegrityViolationException;
import br.renato.payroll.exception.ObjectNotFoundException;
import br.renato.payroll.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository companyRepository;

	private final BankAccountService bankAccountService;
	private final EmployeeService employeeService;
	private final RabbitMQSender sender;

	private static final BigDecimal payrollFee = BigDecimal.valueOf(0.0038);
	private static final String ownerRef = "Payroll System";
	private static final String emailFrom = "renato.guedes14@gmail.com";
	private static final String emailTo = "renato.guedes@somapay.com.br";
	private static final String subject = "Payroll of company paid. ";

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

		return companyRepository.save(new Company(companyDTO.getName(), companyDTO.getCnpj(), companyDTO.getAddress(), companyDTO.getEmail()));
	}

	public Company updateCompany(final Long id, @Valid final CompanyDTO companyDTO) {
		Company company = findCompany(id);

		if (findByCnpj(companyDTO) != null && !Objects.equals(findByCnpj(companyDTO).getId(), id)) {
			throw new DataIntegrityViolationException("CNPJ is already registered in database as another company's CNPJ. ");
		}
		company.setName(companyDTO.getName());
		company.setCnpj(companyDTO.getCnpj());
		company.setAddress(companyDTO.getAddress());
		company.setEmail(companyDTO.getEmail());

		return companyRepository.save(company);
	}

	public void deleteCompany(final Long id) {
		Company company = findCompany(id);

		if (company.getEmployeeList().size() > 0) {
			throw new DataIntegrityViolationException("Company has one or more employees attached to it. ");
		}
		companyRepository.delete(company);
	}

	@Transactional
	public void pay(final Long companyId) {

		Optional<Company> company = companyRepository.findById(companyId);

		if (company.isEmpty()) {
			throw new ObjectNotFoundException("Company not found. ");
		}
		List<EmployeeDTO> employeeDTOList = employeeService.getIdAndSalaryByCompanyId(companyId);
		BankAccountDTO bankAccountDTO = bankAccountService.getCompanyAccountBalance(companyId);
		BigDecimal amountPaid = BigDecimal.ZERO;
		BigDecimal feeAmount = BigDecimal.ZERO;

		if (checkAvailableBalance(bankAccountDTO.getBalance(), employeeDTOList)) {
			throw new DataIntegrityViolationException("Insufficient balance to pay all employees. ");
		}
		for (EmployeeDTO employeeDTO : employeeDTOList) {
			bankAccountService.withdrawalCompanyBankAccount(companyId, employeeDTO.getSalary()
					.add(employeeDTO.getSalary().multiply(payrollFee)));
			amountPaid = amountPaid.add(employeeDTO.getSalary());
			feeAmount = feeAmount.add(employeeDTO.getSalary().multiply(payrollFee));

			try {
				bankAccountService.depositEmployeeBankAccount(employeeDTO.getId(), employeeDTO.getSalary());
			} catch (Exception e) {
				bankAccountService.depositCompanyBankAccount(companyId, employeeDTO.getSalary()
						.add(employeeDTO.getSalary().multiply(payrollFee)));
				throw new DataIntegrityViolationException(e.getMessage());
			}
		}
		sendEmail(company, employeeDTOList, amountPaid, feeAmount);
	}

	private void sendEmail(final Optional<Company> company, final List<EmployeeDTO> paidEmployees,
	                       final BigDecimal amountPaid, final BigDecimal feeAmount) {
		BigDecimal totalPaid = amountPaid.add(feeAmount);

		if (company.isPresent()) {
			EmailDTO emailDTO = new EmailDTO();
			emailDTO.setOwnerRef(ownerRef);
			emailDTO.setEmailFrom(emailFrom);
			emailDTO.setEmailTo(company.get().getEmail());
			emailDTO.setSubject(subject);
			emailDTO.setEmployees(new ArrayList<>());
			for (EmployeeDTO employeeDTO : paidEmployees) {
				emailDTO.getEmployees().add(employeeDTO.getName());
			}
			emailDTO.setText("Payroll of company " + company.get().getName() + " paid. " +
					"\nPaid employees: " + emailDTO.getEmployees().toString() +
					"\nAmount paid: R$" + amountPaid +
					"\nFees: R$" + feeAmount +
					"\nTotal paid: R$" + totalPaid);
			sender.send(emailDTO);
		}
	}

	private boolean checkAvailableBalance(final BigDecimal balance, final List<EmployeeDTO> employeeDTOList) {
		BigDecimal payrollAmount = BigDecimal.ZERO;
		BigDecimal feeAmount;
		for (EmployeeDTO employeeDTO : employeeDTOList) {
			payrollAmount = payrollAmount.add(employeeDTO.getSalary());
		}
		feeAmount = payrollAmount.multiply(payrollFee);
		payrollAmount = payrollAmount.add(feeAmount);

		return balance.compareTo(payrollAmount) < 0;
	}

	private Company findByCnpj(final CompanyDTO companyDTO) {
		return companyRepository.findByCnpj(companyDTO.getCnpj());
	}
}
