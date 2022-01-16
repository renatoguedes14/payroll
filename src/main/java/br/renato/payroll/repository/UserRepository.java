package br.renato.payroll.repository;

import br.renato.payroll.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	boolean existsByUsername(String username);
}
