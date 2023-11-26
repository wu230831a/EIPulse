package com.eipulse.teamproject.dto.formapprovaldto;

import java.time.LocalDateTime;

import com.eipulse.teamproject.entity.formapproval.FormAuditEventLog;
import com.eipulse.teamproject.entity.formapproval.FormRecord;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class FormDTO {
	
	private Integer empId;
	private Integer auditor;
	private Integer[] auditors;
	private Integer formId;
	private Integer eventId;
	private Integer auditEventId;
	private Integer StatusId;
	private Integer typeId;
	private String StatusName;
	private String typeName;
	private String message;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime terminationDate;
	private FormRecordDTO formRecordDTO;
	private Object form;

	public FormDTO(FormAuditEventLog formAuditEventLog) {
		this.formId = formAuditEventLog.getFormEventLog().getFormRecord().getFormId();
		this.auditor = formAuditEventLog.getAuditor();
		this.empId = formAuditEventLog.getFormEventLog().getFormRecord().getEmpId();
		this.auditEventId = formAuditEventLog.getEventId();
		this.typeName = formAuditEventLog.getFormEventLog().getFormRecord().getFormType().getTypeName();
		this.typeId = formAuditEventLog.getFormEventLog().getFormRecord().getTypeId();
		this.StatusName = formAuditEventLog.getFormStatus().getDescription();
		this.StatusId = formAuditEventLog.getStatusId();
		this.startDate = formAuditEventLog.getFormEventLog().getStartDate();
		this.endDate = formAuditEventLog.getEndDate();
		this.message = formAuditEventLog.getMessage();
		switch (formAuditEventLog.getFormEventLog().getFormRecord().getFormType().getTypeId()){
			case 1:
				this.form = new FormLeaveDTO(formAuditEventLog.getFormEventLog().getFormRecord().getLeaveTable());
				break;
			case 2:
				this.form = new FormOvertimeDTO(formAuditEventLog.getFormEventLog().getFormRecord().getOvertime());
				break;
			case 3:
				this.form = new FormResignDTO(formAuditEventLog.getFormEventLog().getFormRecord().getResignation());
				break;
		}
	}

	public FormDTO(FormRecord formRecord) {
		this.formId = formRecord.getFormId();
		this.auditor = formRecord.getFormEventLog().get(0).getFormAuditEventLog().get(0).getAuditor();
		this.empId = formRecord.getEmpId();
		this.typeName = formRecord.getFormType().getTypeName();
		this.typeId = formRecord.getTypeId();
		this.StatusName = formRecord.getFormStatus().getDescription();
		this.StatusId = formRecord.getStatusId();
		this.startDate = formRecord.getStartDate();
		this.endDate = formRecord.getEndDate();
		this.terminationDate = formRecord.getTerminationDate();
		this.message = formRecord.getFormEventLog().get(0).getFormAuditEventLog().get(0).getMessage();
		switch (formRecord.getFormType().getTypeId()){
			case 1:
				this.form = new FormLeaveDTO(formRecord.getLeaveTable());
				break;
			case 2:
				this.form = new FormOvertimeDTO(formRecord.getOvertime());
				break;
			case 3:
				this.form = new FormResignDTO(formRecord.getResignation());
				break;
		}
	}


}
