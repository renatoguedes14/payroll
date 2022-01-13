package br.renato.payroll.resources;

import br.renato.payroll.dtos.EmployeeDTO;
import br.renato.payroll.entities.Employee;
import br.renato.payroll.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/employees")
@RestController
@RequestMapping(value = "/employee")
@RequiredArgsConstructor
public class EmployeeResource {

	private final EmployeeService employeeService;

	@ApiOperation(value = "Finds an employee by its id. ")
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> find(@PathVariable final Long id) {
		EmployeeDTO employeeDTO = new EmployeeDTO(employeeService.findEmployee(id));

		return ResponseEntity.ok().body(employeeDTO);
	}

	@ApiOperation(value = "Finds all registered companies. ")
	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> findAll() {
		List<EmployeeDTO> employeeDTOList = employeeService.findAll().stream().map(EmployeeDTO::new)
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(employeeDTOList);
	}

	@ApiOperation(value = "Creates an employee. ")
	@PostMapping
	public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody final EmployeeDTO employeeDTO) {
		Employee employee = employeeService.create(employeeDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Updates an already created employee. ")
	public ResponseEntity<EmployeeDTO> update(@PathVariable final Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO newEmployee = new EmployeeDTO(employeeService.update(id, employeeDTO));

		return ResponseEntity.ok().body(newEmployee);
	}

	@ApiOperation(value = "Deletes the desired employee by its id. ")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable final Long id) {
		employeeService.delete(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Checks the employee's balance. ")
	@GetMapping("/balance/{id}")
	public ResponseEntity<BigDecimal> checkBalance(@PathVariable final Long id) {
		EmployeeDTO employeeDTO = new EmployeeDTO(employeeService.checkBalance(id));

		return ResponseEntity.ok().body(employeeDTO.getBalance());
	}
}
