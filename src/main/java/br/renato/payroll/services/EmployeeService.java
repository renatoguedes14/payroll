package br.renato.payroll.services;

import br.renato.payroll.dtos.EmployeeDTO;
import br.renato.payroll.entities.Company;
import br.renato.payroll.entities.Employee;
import br.renato.payroll.exceptions.DataIntegrityViolationException;
import br.renato.payroll.exceptions.ObjectNotFoundException;
import br.renato.payroll.repository.CompanyRepository;
import br.renato.payroll.repository.EmployeeRepository;
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
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final CompanyRepository companyRepository;

	public Employee findEmployee(final Long id) {
		Optional<Employee> employee = employeeRepository.findById(id);

		return employee.orElseThrow(() -> new ObjectNotFoundException("Employee not found. "));
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Employee create(final EmployeeDTO employeeDTO) {
		if (findByCpf(employeeDTO) != null) {
			throw new DataIntegrityViolationException("Employee already found. ");
		}
		Optional<Company> company = companyRepository.findById(employeeDTO.getCompanyId());

		if (company.isEmpty()) {
			throw new ObjectNotFoundException("Company not found. ");
		}

		return employeeRepository.save(new Employee(employeeDTO.getName(), employeeDTO.getCpf(), employeeDTO.getAddress(),
				employeeDTO.getBalance(), company.get()));
	}

	public Employee update(final Long id, @Valid EmployeeDTO employeeDTO) {
		Employee employee = findEmployee(id);

		if (findByCpf(employeeDTO) != null && !Objects.equals(findByCpf(employeeDTO).getId(), id)) {
			throw new DataIntegrityViolationException("CPF already found in another employee's registry. ");
		}
		Optional<Company> company = companyRepository.findById(employeeDTO.getCompanyId());

		if (company.isEmpty()) {
			throw new ObjectNotFoundException("Company not found. ");
		}

		employee.setName(employeeDTO.getName());
		employee.setCpf(employeeDTO.getCpf());
		employee.setAddress(employeeDTO.getAddress());
		employee.setBalance(employeeDTO.getBalance());
		employee.setCompany(company.get());

		return employeeRepository.save(employee);
	}

	public void delete(final Long id) {
		Employee employee = findEmployee(id);
		employeeRepository.delete(employee);
	}

	public Employee checkBalance(final Long id) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);

		return employeeOptional.orElseThrow(() -> new ObjectNotFoundException("Employee not found. "));
	}

	private Employee findByCpf(final EmployeeDTO employeeDTO) {
		return employeeRepository.findByCpf(employeeDTO.getCpf());
	}
}
