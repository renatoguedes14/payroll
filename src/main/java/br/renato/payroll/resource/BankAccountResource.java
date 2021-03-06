package br.renato.payroll.resource;

import br.renato.payroll.dto.BankAccountDTO;
import br.renato.payroll.entity.BankAccount;
import br.renato.payroll.service.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/bankAccounts", tags = "Allows you to retrieve and create Bank Accounts and pay payrolls. ")
@RestController
@RequestMapping(value = "/bankAccounts")
@RequiredArgsConstructor
public class BankAccountResource {

	private final BankAccountService bankAccountService;

	@ApiOperation(value = "Allows you to find a bank account by its id. ", response = BankAccountDTO.class)
	@GetMapping("/{id}")
	public ResponseEntity<BankAccountDTO> find(@PathVariable final Long id) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO(bankAccountService.findBankAccount(id));

		return ResponseEntity.ok().body(bankAccountDTO);
	}

	@ApiOperation(value = "Allows you to retrieve all bank accounts. ", response = BankAccountDTO.class, responseContainer = "List")
	@GetMapping("/all")
	public ResponseEntity<List<BankAccountDTO>> findAll() {
		List<BankAccountDTO> bankAccountDTOList = bankAccountService.findAllBankAccounts().stream().map(BankAccountDTO::new)
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(bankAccountDTOList);
	}

	@ApiOperation(value = "Allows you to check a company bank account's balance. ", response = BankAccountDTO.class)
	@GetMapping("/company/checkBalance/{companyId}")
	public ResponseEntity<BankAccountDTO> checkCompanyAccountBalance(@PathVariable final Long companyId) {
		BankAccountDTO bankAccountDTO = bankAccountService.getCompanyAccountBalance(companyId);

		return ResponseEntity.ok().body(bankAccountDTO);
	}

	@ApiOperation(value = "Allows you to check a employee bank account's balance. ", response = BankAccountDTO.class)
	@GetMapping("/employee/checkBalance/{employeeId}")
	public ResponseEntity<BankAccountDTO> checkEmployeeAccountBalance(@PathVariable final Long employeeId) {
		BankAccountDTO bankAccountDTO = bankAccountService.getEmployeeAccountBalance(employeeId);

		return ResponseEntity.ok().body(bankAccountDTO);
	}

	@ApiOperation(value = "Create a new bank account. ")
	@PostMapping("/create")
	public ResponseEntity<BankAccountDTO> create(@RequestBody final BankAccountDTO bankAccountDTO) {
		BankAccount bankAccount = bankAccountService.save(bankAccountDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bankAccount.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}
}
