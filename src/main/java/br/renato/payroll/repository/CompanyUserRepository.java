package br.renato.payroll.repository;


import br.renato.payroll.entities.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {

	Optional<CompanyUser> findByEmail(String email);

	@Transactional
	@Modifying
	@Query("UPDATE CompanyUser obj SET obj.enabled = TRUE WHERE obj.email = ?1")
	void enableCompanyUser(String email);
}
