package com.eipulse.teamproject.repository.formapprovalrepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.eipulse.teamproject.entity.formapproval.FormRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormRecordRepository extends JpaRepository<FormRecord, Integer> {

	@Query(value="from FormRecord where empId = :id and (:statusId is null or statusId = :statusId) and (:startTime is null or startDate >= :startTime) and (:endTime is null or startDate <= :endTime)")
	Page<FormRecord> findMyForm(Integer id, Integer statusId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
	@Query(value="from FormRecord where empId = ?1 and formId = ?2")
	FormRecord findEmpForm(Integer empId,Integer formId);
	@Query(value="from FormRecord where empId = ?1 and typeId=3")
	List<FormRecord> findCheckEmpForm(Integer id);

	@Query(value="from FormRecord where empId = ?1")
	Page<FormRecord> findCheckEmpForm(Integer id, Pageable pageable);

	//查詢請假
	@Query("from FormRecord fr JOIN fr.leaveTable leave where (:empId is null or fr.empId = :empId) and fr.typeId = 1 and (:statusId is null or fr.statusId = :statusId) and (:startTime is null or fr.startDate >= :startTime) and (:endTime is null or fr.startDate <= :endTime) and (:lvtype is null or leave.leaveType.typeId = :lvtype)")
	Page<FormRecord> findLeave(Integer empId, Integer statusId, LocalDateTime startTime, LocalDateTime endTime, Integer lvtype, Pageable pageable);

	//查詢加班
	@Query("from FormRecord fr JOIN fr.overtime overtime where (:empId is null or fr.empId = :empId) and fr.typeId = 2 and (:statusId is null or fr.statusId = :statusId) and (:startTime is null or fr.startDate >= :startTime) and (:endTime is null or fr.startDate <= :endTime) and (:ovtype is null or overtime.overtimeType.typeId = :ovtype)")
	Page<FormRecord> findOvertime(Integer empId, Integer statusId, LocalDateTime startTime, LocalDateTime endTime, Integer ovtype, Pageable pageable);

	@Query("from FormRecord fr where (:empId is null or fr.empId = :empId) and fr.typeId = 3 and (:statusId is null or fr.statusId = :statusId) and (:startTime is null or fr.startDate >= :startTime) and (:endTime is null or fr.startDate <= :endTime)")
	Page<FormRecord> findResignation(Integer empId, Integer statusId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

	//過期表單
	@Query("from FormRecord where terminationDate >= :startDateTime and terminationDate < :endDateTime ORDER BY terminationDate DESC")
	List<FormRecord> findFormRecordDate(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

	@Query("SELECT (lt.days*24 - COALESCE(SUM(leave.days*24+leave.hours), 0)) AS Result FROM FormRecord fr join fr.leaveTable leave join leave.leaveType lt WHERE fr.statusId IN (1, 2) AND lt.typeId = :lvtype AND MONTH(leave.startTime) = :month AND fr.empId = :empId")
	Integer getRemainingLeaveDays(Integer lvtype, Integer month, Integer empId);



	//查詢是否有重複表單?
	@Query("select count(*)>0 from FormRecord where empId = :empId and statusId IN (1, 2) and typeId = 3")
	boolean findCountResignation(Integer empId);

	@Query("from FormRecord fr join fr.resignation res where res.leaveDate = :leDate and fr.statusId = 2")
	List<FormRecord> findLeaveResignation(LocalDate leDate);

}
