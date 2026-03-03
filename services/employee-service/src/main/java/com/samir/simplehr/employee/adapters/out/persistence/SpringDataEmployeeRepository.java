package com.samir.simplehr.employee.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataEmployeeRepository extends JpaRepository<EmployeeJpaEntity, UUID> {
	boolean existsByEmail(String email);
}
