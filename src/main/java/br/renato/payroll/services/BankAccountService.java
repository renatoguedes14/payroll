package br.renato.payroll.services;

import br.renato.payroll.dtos.BankAccountDTO;
import br.renato.payroll.entities.BankAccount;
import br.renato.payroll.entities.Company;
import br.renato.payroll.entities.Employee;
import br.renato.payroll.exceptions.DataIntegrityViolationException;
import br.renato.payroll.exceptions.ObjectNotFoundException;
import br.renato.payroll.repositories.BankAccountRepository;
import br.renato.payroll.repositories.CompanyRepository;
import br.renato.payroll.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

	private final BankAccountRepository bankAccountRepository;
	private final CompanyRepository companyRepository;
	private final EmployeeRepository employeeRepository;

	public BankAccount findBankAccount(Long id) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
		return bankAccount.orElseThrow(() -> new ObjectNotFoundException(
				"Bank account not found. "));
	}

	public List<BankAccount> findAllBankAccounts() {
		return bankAccountRepository.findAll();
	}

	public BankAccount save(BankAccountDTO bankAccountDTO) {
		if (bankAccountDTO.getBalance() == null) {
			bankAccountDTO.setBalance(BigDecimal.ZERO);
		}
		if (bankAccountDTO.getCompanyId() == null && bankAccountDTO.getEmployeeId() == null) {
			throw new DataIntegrityViolationException("The bank account must have a company or an employee. ");
		}
		if (bankAccountDTO.getCompanyId() != null && bankAccountDTO.getEmployeeId() != null) {
			throw new DataIntegrityViolationException("The bank account can only be attached to either a company OR an employee. ");
		}
		if (bankAccountDTO.getCompanyId() != null) {
			if (bankAccountRepository.existsByCompanyId(bankAccountDTO.getCompanyId())) {
				throw new DataIntegrityViolationException("This company already has a bank account. ");
			}
			Optional<Company> companyOptional = companyRepository.findById(bankAccountDTO.getCompanyId());
			if (companyOptional.isEmpty()) {
				throw new ObjectNotFoundException("Company not found. ");
			}
			return bankAccountRepository.save(new BankAccount(bankAccountDTO.getBalance(), companyOptional.get()));
		} else {
			if (bankAccountRepository.existsByEmployeeId(bankAccountDTO.getEmployeeId())) {
				throw new DataIntegrityViolationException("This employee already has a bank account. ");
			}
			Optional<Employee> employeeOptional = employeeRepository.findById(bankAccountDTO.getEmployeeId());
			if (employeeOptional.isEmpty()) {
				throw new ObjectNotFoundException("Employee not found. ");
			}
			return bankAccountRepository.save(new BankAccount(bankAccountDTO.getBalance(), employeeOptional.get()));
		}
	}

	public BankAccountDTO getCompanyAccountBalance(Long companyId) {
		BankAccount bankAccount = bankAccountRepository.findByCompanyId(companyId);
		if (bankAccount == null) {
			throw new ObjectNotFoundException("Bank account not found. ");
		}
		return new BankAccountDTO(bankAccount.getId(), bankAccount.getBalance());
	}

	public BankAccountDTO getEmployeeAccountBalance(Long employeeId) {
		BankAccount bankAccount = bankAccountRepository.findByEmployeeId(employeeId);
		if (bankAccount == null) {
			throw new ObjectNotFoundException("Bank account not found. ");
		}
		return new BankAccountDTO(bankAccount.getId(), bankAccount.getBalance());
	}

	@Transactional
	public void withdrawalCompanyBankAccount(Long companyId, BigDecimal amount) {
		BankAccount bankAccount = bankAccountRepository.findByCompanyId(companyId);
		BigDecimal balance = bankAccount.getBalance().subtract(amount);
		bankAccount.setBalance(balance);

		bankAccountRepository.save(bankAccount);
//		bankAccountRepository.updateBalance(bankAccountDTO.getId(), balance);
	}

	@Transactional
	public void depositCompanyBankAccount(Long companyId, BigDecimal amount) {
		BankAccountDTO bankAccountDTO = getCompanyAccountBalance(companyId);
		BigDecimal balance = bankAccountDTO.getBalance().add(amount);

		bankAccountRepository.updateBalance(bankAccountDTO.getId(), balance);
	}

	@Transactional
	public void depositEmployeeBankAccount(Long employeeId, BigDecimal amount) {
		BankAccountDTO bankAccountDTO = getEmployeeAccountBalance(employeeId);
		BigDecimal balance = bankAccountDTO.getBalance().add(amount);

		bankAccountRepository.updateBalance(bankAccountDTO.getId(), balance);
	}
}
