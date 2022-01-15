package br.renato.payroll.resource;

import br.renato.payroll.dto.CompanyDTO;
import br.renato.payroll.entity.Company;
import br.renato.payroll.exception.DataIntegrityViolationException;
import br.renato.payroll.service.CompanyService;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/companies", tags = "Allows you to retrieve, create, update or delete companies. You can also pay a payroll. ")
@RestController
@RequestMapping(value = "/companies")
@RequiredArgsConstructor
public class CompanyResource {

	private final CompanyService companyService;

	@ApiOperation(value = "Allows you to find a company by its id. ", response = CompanyDTO.class)
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDTO> find(@PathVariable final Long id) {
		CompanyDTO companyDTO = new CompanyDTO(companyService.findCompany(id));

		return ResponseEntity.ok().body(companyDTO);
	}

	@ApiOperation(value = "Allows you to retrieve all companies. ", response = CompanyDTO.class, responseContainer = "List")
	@GetMapping("/all")
	public ResponseEntity<List<CompanyDTO>> findAll() {
		List<CompanyDTO> companyDTOList = companyService.findAllCompanies().stream().map(CompanyDTO::new)
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(companyDTOList);
	}

	@ApiOperation(value = "Create a new company. ")
	@PostMapping("/create")
	public ResponseEntity<CompanyDTO> create(@Valid @RequestBody final CompanyDTO companyDTO) {
		Company company = companyService.createCompany(companyDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(company.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Update an existing company. ")
	@PutMapping("/update/{id}")
	public ResponseEntity<CompanyDTO> update(@PathVariable final Long id, @Valid @RequestBody CompanyDTO companyDTO) {
		CompanyDTO newCompany = new CompanyDTO(companyService.updateCompany(id, companyDTO));

		return ResponseEntity.ok().body(newCompany);
	}

	@ApiOperation(value = "Delete a company. ")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable final Long id) {
		companyService.deleteCompany(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Allows you to pay all of a company's employees. ")
	@PutMapping("/payroll/pay/{id}")
	@Transactional
	public ResponseEntity<Void> pay(@PathVariable Long id) {
		try {
			companyService.pay(id);
		} catch (Exception e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
		return ResponseEntity.noContent().build();
	}
}
