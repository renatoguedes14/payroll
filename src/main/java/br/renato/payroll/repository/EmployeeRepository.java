package br.renato.payroll.repository;

import br.renato.payroll.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("SELECT obj FROM Employee obj WHERE obj.cpf =:cpf")
	Employee findByCpf(@Param("cpf") final String cpf);

	List<Employee> findByCompanyId(final Long companyId);

}
