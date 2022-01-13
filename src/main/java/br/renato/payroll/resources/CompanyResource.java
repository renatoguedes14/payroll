package br.renato.payroll.resources;

import br.renato.payroll.configs.SpringFoxConfig;
import br.renato.payroll.dtos.CompanyDTO;
import br.renato.payroll.dtos.TransactionDTO;
import br.renato.payroll.entities.Company;
import br.renato.payroll.services.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/companies", tags = SpringFoxConfig.COMPANY_API_DESCRIPTION_TAG)
@RestController
@RequestMapping(value = "/companies")
@RequiredArgsConstructor
public class CompanyResource {

	private final CompanyService companyService;

	@ApiOperation(value = "Searches a company by its id. ", response = CompanyDTO.class)
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDTO> find(@PathVariable final Long id) {
		CompanyDTO companyDTO = new CompanyDTO(companyService.findCompany(id));

		return ResponseEntity.ok().body(companyDTO);
	}

	@ApiOperation(value = "Searches all companies in database. ", response = CompanyDTO.class, responseContainer = "List")
	@GetMapping
	public ResponseEntity<List<CompanyDTO>> findAll() {
		List<CompanyDTO> companyDTOList = companyService.findAllCompanies().stream().map(obj -> new CompanyDTO(obj))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(companyDTOList);
	}

	@ApiOperation(value = "Creates a company with its data in requisition's body. ")
	@PostMapping
	public ResponseEntity<CompanyDTO> create(@Valid @RequestBody final CompanyDTO companyDTO) {
		Company company = companyService.createCompany(companyDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(company.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Updates the company's data. ")
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDTO> update(@PathVariable final Long id, @Valid @RequestBody CompanyDTO companyDTO) {
		CompanyDTO newCompany = new CompanyDTO(companyService.updateCompany(id, companyDTO));

		return ResponseEntity.ok().body(newCompany);
	}

	@ApiOperation(value = "Deletes the desired company by its id. ")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable final Long id) {
		companyService.deleteCompany(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Checks company's balance. ")
	@GetMapping("/balance/{id}")
	public ResponseEntity<BigDecimal> checkBalance(@PathVariable final Long id) {
		CompanyDTO companyDTO = new CompanyDTO(companyService.checkBalance(id));

		return ResponseEntity.ok().body(companyDTO.getBalance());
	}

	@ApiOperation(value = "Transfers any amount to desired employee. ")
	@PostMapping("/transfer")
	public ResponseEntity<?> transfer(@Valid @RequestBody final TransactionDTO transactionDTO) {
		companyService.transfer(transactionDTO);

		return ResponseEntity.ok("Transfer successfully made. ");
	}
}
