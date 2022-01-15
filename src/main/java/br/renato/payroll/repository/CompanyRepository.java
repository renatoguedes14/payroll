package br.renato.payroll.repository;

import br.renato.payroll.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT obj FROM Company obj WHERE obj.cnpj =:cnpj")
	Company findByCnpj(@Param("cnpj") String cnpj);
}
