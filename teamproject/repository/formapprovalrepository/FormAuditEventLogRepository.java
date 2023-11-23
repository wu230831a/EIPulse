package com.eipulse.teamproject.repository.formapprovalrepository;
import com.eipulse.teamproject.entity.formapproval.FormAuditEventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FormAuditEventLogRepository extends JpaRepository<FormAuditEventLog, Integer> {

	@Query(value = "from FormAuditEventLog where auditor = ?1 and StatusId = 1")
	Page<FormAuditEventLog> findUncompletedForm(Integer id, Pageable pageable);

	@Query(value = "from FormAuditEventLog where auditor = ?1 and StatusId NOT IN (4, 1)")
	Page<FormAuditEventLog> findCompletedForm(Integer id, Pageable pageable);

	@Query("FROM FormAuditEventLog audit JOIN audit.formEventLog e WHERE e.eventId = ?1")
	List<FormAuditEventLog> findAuditCheck(Integer eventId);

	@Query("FROM FormAuditEventLog audit JOIN audit.formEventLog e WHERE e.eventId = ?1 and audit.eventId != ?2 ")
	List<FormAuditEventLog> findAuditCheck(Integer eventId,Integer auditEventId);

}
