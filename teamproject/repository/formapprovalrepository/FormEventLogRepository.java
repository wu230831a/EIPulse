package com.eipulse.teamproject.repository.formapprovalrepository;
import com.eipulse.teamproject.entity.formapproval.FormEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FormEventLogRepository extends JpaRepository<FormEventLog, Integer> {

	@Query(value = "from FormEventLog e JOIN e.formRecord fr where fr.formId = ?1")
	FormEventLog findEventForm(Integer id);
	
	@Query(value = "from FormEventLog fe join fe.formRecord fr where fe.sequence = ?1 AND fr.formId = ?2")
	FormEventLog findNextEventForm(Integer count,Integer formId);
}
