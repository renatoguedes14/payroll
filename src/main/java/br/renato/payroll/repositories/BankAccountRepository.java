package br.renato.payroll.repositories;

import br.renato.payroll.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

	boolean existsByCompanyId(Long companyId);
	boolean existsByEmployeeId(Long employeeId);

	BankAccount findByCompanyId(Long companyId);
	BankAccount findByEmployeeId(Long employeeId);

	@Transactional
	@Modifying
	@Query("UPDATE BankAccount obj SET obj.balance = :balance WHERE obj.id = :id")
	void updateBalance(Long id, BigDecimal balance);
}
