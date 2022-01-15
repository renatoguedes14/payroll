package br.renato.payroll.repositories;

import br.renato.payroll.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("SELECT obj FROM Employee obj WHERE obj.cpf =:cpf")
	Employee findByCpf(@Param("cpf") String cpf);

	Optional<Employee> getByCpf(String cpf);
}
