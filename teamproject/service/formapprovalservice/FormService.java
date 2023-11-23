package com.eipulse.teamproject.service.formapprovalservice;

import com.eipulse.teamproject.entity.employee.ResignRecord;
import com.eipulse.teamproject.entity.formapproval.*;
import com.eipulse.teamproject.repository.employeerepository.ResignRecordRepository;
import com.eipulse.teamproject.repository.formapprovalrepository.FormAuditEventLogRepository;
import com.eipulse.teamproject.repository.formapprovalrepository.FormEventLogRepository;
import com.eipulse.teamproject.repository.formapprovalrepository.FormRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FormService {
	private FormRecordRepository frRepo;
	private FormAuditEventLogRepository auditRepo;
	private FormEventLogRepository eventRepo;
	private ResignRecordRepository resRepo;

	@Autowired
	public FormService(FormRecordRepository frRepo, FormAuditEventLogRepository auditRepo, FormEventLogRepository eventRepo, ResignRecordRepository resRepo) {
		this.frRepo = frRepo;
		this.auditRepo = auditRepo;
		this.eventRepo = eventRepo;
		this.resRepo = resRepo;
	}

	public FormRecord createForm(Integer empId, Integer audit, Integer formType) {
		FormRecord form = new FormRecord();
		form.setEmpId(empId);
		form.setTypeId(formType);
		form.setStatusId(1);

		List<FormEventLog> listfe = new ArrayList<>();
		List<FormAuditEventLog> listfae = new ArrayList<>();
		FormEventLog fel = new FormEventLog();
		FormAuditEventLog formAudit = new FormAuditEventLog();
		fel.setFormRecord(form);
		fel.setFormAuditEventLog(listfae);
		fel.setSequence(0);
		fel.setHeadCount(0);
		fel.setStatusId(1);
		formAudit.setAuditor(audit);
		formAudit.setFormEventLog(fel);
		formAudit.setStatusId(1);
		listfae.add(formAudit);
		listfe.add(fel);

		form.setFormEventLog(listfe);
		return form;
	}
	public FormRecord createForm(Integer empId,Integer[] audits,Integer formType) {
		FormRecord form = new FormRecord();
		form.setEmpId(empId);
		form.setTypeId(formType);
		form.setStatusId(1);

		List<FormEventLog> listfe = new ArrayList<>();
		List<FormAuditEventLog> listfae = new ArrayList<>();
		FormEventLog fel = new FormEventLog();
		fel.setFormRecord(form);
		fel.setFormAuditEventLog(listfae);
		fel.setSequence(0);
		fel.setHeadCount(0);
		fel.setStatusId(1);
		for(int audit: audits ) {
			FormAuditEventLog formAudit = new FormAuditEventLog();
			formAudit.setAuditor(audit);
			formAudit.setFormEventLog(fel);
			formAudit.setStatusId(1);
			listfae.add(formAudit);
		}
		listfe.add(fel);
		form.setFormEventLog(listfe);
		return form;
	}

	public Leave returnLeave(Integer id) {
		return frRepo.findById(id).get().getLeaveTable();
	}

	public Overtime returnOvertime(Integer id) {
		return frRepo.findById(id).get().getOvertime();
	}

	public Resignation returnResignRecord(Integer id) {	return frRepo.findById(id).get().getResignation(); }

	public void revokeForm(Integer id,Integer formId){
		FormRecord formRecord = frRepo.findEmpForm(id,formId);
		formRecord.setFormEventLog(formRecord.getFormEventLog().stream()
				.map(formEventLog -> {
					formEventLog.setStatusId(6);

					formEventLog.setFormAuditEventLog(formEventLog.getFormAuditEventLog().stream()
							.map(formAuditEventLog -> {
								formAuditEventLog.setStatusId(6);
								return formAuditEventLog;
							})
							.collect(Collectors.toList()));

					return formEventLog;
				})
				.collect(Collectors.toList()));
		formRecord.setStatusId(6);
		formRecord.setEndDate(LocalDateTime.now());
		frRepo.save(formRecord);
	}

	public FormAuditEventLog checkEmpForm(Integer yn,FormAuditEventLog newauditLog){
		FormAuditEventLog auditLog = auditRepo.findById(newauditLog.getEventId()).get();

		if (yn != 0) {
			auditLog.setStatusId(2);
		} else {
			auditLog.setStatusId(3);
		}
		LocalDateTime date = LocalDateTime.now();
		auditLog.setEndDate(date);
		if (newauditLog.getMessage() != null)
			auditLog.setMessage(newauditLog.getMessage());
		auditRepo.save(auditLog);
		// 判斷當前流程還有沒有人沒審核
		List<FormAuditEventLog> forms = auditRepo.findAuditCheck(auditLog.getFormEventLog().getEventId());
		Integer yes = 0;
		Integer no = 0;
		for (FormAuditEventLog form : forms) {
			if (form.getStatusId() == 1)
				return auditLog;
			else if (form.getStatusId() == 2)
				yes++;
			else if (form.getStatusId() == 3)
				no++;
		}
		// 更新流程的狀態
		FormEventLog eventLog = eventRepo.findById(auditLog.getFormEventLog().getEventId()).get();
		eventLog.setEndDate(date);
		if (yes > no || yes > eventLog.getHeadCount()) {
			eventLog.setStatusId(2);
		} else {
			eventLog.setStatusId(3);
		}
		eventRepo.save(eventLog);
		// 查詢是否有下一個流程
		FormEventLog nextEventLog = eventRepo.findNextEventForm(eventLog.getSequence() + 1,eventLog.getFormRecord().getFormId());
		if (nextEventLog == null) {
			FormRecord upfr = eventLog.getFormRecord();
			upfr.setEndDate(date);
			upfr.setStatusId(eventLog.getStatusId());


			//表單結束後執行
			if(upfr.getTypeId()==3 & upfr.getStatusId()==2){
				setResign(upfr);
			}


			frRepo.save(upfr);
			return auditLog;
		} else if(eventLog.getStatusId() == 3) {
			FormRecord upfr = eventLog.getFormRecord();
			upfr.setEndDate(date);
			upfr.setStatusId(3);
			frRepo.save(upfr);
			return auditLog;
		} else {
			nextEventLog.setStartDate(date);
			nextEventLog.setStatusId(1);
			List<FormAuditEventLog> nextAudit = nextEventLog.getFormAuditEventLog();
			List<FormAuditEventLog> upAudit = new ArrayList<>();
			for (FormAuditEventLog audit : nextAudit) {
				audit.setStatusId(1);
				audit.setFormEventLog(nextEventLog);
				upAudit.add(audit);
			}
			nextEventLog.setFormAuditEventLog(upAudit);
			eventRepo.save(nextEventLog);
		}
		return auditLog;
	}

	private void setResign(FormRecord form){

		resRepo.save(new ResignRecord(form));
	}

}
