package com.samir.simplehr.leave.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataLeaveRequestRepository extends JpaRepository<LeaveRequestJpaEntity, UUID> {

	@Query("""
			SELECT COUNT(l) > 0
			FROM LeaveRequestJpaEntity l
			WHERE l.employeeId = :employeeId
			  AND l.status = com.samir.simplehr.leave.domain.model.LeaveStatus.APPROVED
			  AND l.startDate <= :endDate
			  AND l.endDate >= :startDate
			""")
	boolean existsApprovedOverlap(
			@Param("employeeId") UUID employeeId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate
	);

	@Query("""
			SELECT l
			FROM LeaveRequestJpaEntity l
			WHERE l.employeeId = :employeeId
			  AND l.status = com.samir.simplehr.leave.domain.model.LeaveStatus.APPROVED
			  AND l.startDate >= :yearStart
			  AND l.endDate <= :yearEnd
			""")
	List<LeaveRequestJpaEntity> findApprovedByEmployeeAndYear(
			@Param("employeeId") UUID employeeId,
			@Param("yearStart") LocalDate yearStart,
			@Param("yearEnd") LocalDate yearEnd
	);
}
