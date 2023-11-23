package com.eipulse.teamproject.controller.formapprovalcontroller;


import com.eipulse.teamproject.dto.formapprovaldto.FormDTO;
import com.eipulse.teamproject.dto.formapprovaldto.FormLeaveDTO;
import com.eipulse.teamproject.dto.formapprovaldto.FormOvertimeDTO;
import com.eipulse.teamproject.dto.formapprovaldto.FormResignDTO;
import com.eipulse.teamproject.entity.formapproval.*;
import com.eipulse.teamproject.repository.formapprovalrepository.FormAuditEventLogRepository;
import com.eipulse.teamproject.repository.formapprovalrepository.FormRecordRepository;
import com.eipulse.teamproject.service.formapprovalservice.FileService;
import com.eipulse.teamproject.service.formapprovalservice.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class FormController {
	private FormRecordRepository frRepo;
	private FormAuditEventLogRepository auditRepo;
	private FormService formService;
	private FileService fileService;
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	public FormController(FormRecordRepository frRepo, FormAuditEventLogRepository auditRepo, FormService formService, FileService fileService, SimpMessagingTemplate messagingTemplate) {
		this.frRepo = frRepo;
		this.auditRepo = auditRepo;
		this.formService = formService;
		this.fileService = fileService;
		this.messagingTemplate = messagingTemplate;
	}

	//	寫一個加班表單
	@PostMapping("/form/applyForOvertime")
	public ResponseEntity<?> applyForOvertime(@RequestParam Integer audit, @RequestParam Integer empId,
											  @RequestPart(value = "files", required = false) List<MultipartFile> files,
											  @RequestPart(value = "data") Overtime overTime) throws IOException {
		if(files!=null)
		fileService.uploadFormImage(files);

		FormRecord form = formService.createForm(empId, audit,2);
		form.setOvertime(overTime);
		overTime.setFormRecord(form);
		frRepo.save(form);

		return new ResponseEntity<>(HttpStatus.OK);
	}

//	寫一個請假表單
	@PostMapping("/form/applyForLeave")
	public ResponseEntity<?> applyForLeave(@RequestParam Integer audit,@RequestParam Integer empId,
									@RequestPart(value = "files", required = false) List<MultipartFile> files,
									@RequestPart("data") Leave leave) throws IOException {
		if(files!=null)
		fileService.uploadFormImage(files);

		FormRecord form = formService.createForm(empId, audit,1);
		form.setLeaveTable(leave);
		leave.setFormRecord(form);
		frRepo.save(form);

		return new ResponseEntity<>(HttpStatus.OK);
	}

//	寫一個離職單
	@PostMapping("/form/applyResign")
	public ResponseEntity<?> applyResign(@RequestParam Integer audit, @RequestParam Integer empId,
								  @RequestPart(value = "files", required = false) List<MultipartFile> files,
								  @RequestPart("data") Resignation resignation) throws IOException {
		if(frRepo.findCountResignation(empId)){
			return new ResponseEntity<>(false,HttpStatus.OK);
		}
		if(files!=null)
		fileService.uploadFormImage(files);

		FormRecord form = formService.createForm(empId, audit,3);
		form.setResignation(resignation);
		resignation.setFormRecord(form);
		frRepo.save(form);
		return new ResponseEntity<>(true,HttpStatus.OK);
	}
	
//	找出所有需要你簽核的資料(已完成與未完成
	@GetMapping("/form/uncompletedAuditForm")
	public ResponseEntity<?> uncompletedAuditForm(@RequestParam Integer id, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<FormDTO> formDTOS = auditRepo.findUncompletedForm(id,pageable).map(fa -> new FormDTO(fa));
		return new ResponseEntity<>(formDTOS,HttpStatus.OK);
	}
	@GetMapping("/form/completedAuditForm")
	public ResponseEntity<?> completedAuditForm(@RequestParam Integer id, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<FormDTO> formDTOS = auditRepo.findCompletedForm(id,pageable).map(fa -> new FormDTO(fa));
		return new ResponseEntity<>(formDTOS,HttpStatus.OK);
	}

//	查看表單詳細資料
	@GetMapping("/form/findCheckEmpForm")
	public ResponseEntity<?> findCheckEmpForm(@RequestParam Integer formId) {
		FormRecord form = frRepo.findById(formId).get();
		int type = form.getTypeId();
		switch (type) {
			case 1: {
				return new ResponseEntity<>(new FormLeaveDTO(formService.returnLeave(form.getFormId())), HttpStatus.OK);
			}
			case 2: {
				return new ResponseEntity<>(new FormOvertimeDTO(formService.returnOvertime(form.getFormId())), HttpStatus.OK);
			}
			case 3: {
				return new ResponseEntity<>(new FormResignDTO(formService.returnResignRecord(form.getFormId())), HttpStatus.OK);
			}
			default:{
				return null;
			}
		}
	}

	@PostMapping("/form/selectForms")
	public ResponseEntity<?> selectForms(@RequestBody FormSelect fs, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
		Page<FormRecord> formRecords = null;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		switch (fs.getFormType()){
			case 0:
				formRecords = frRepo.findMyForm(fs.getEmpId(),fs.getStustId(),fs.getStartTime(),fs.getEndTime(),pageable);
				break;
			case 1:
				formRecords = frRepo.findLeave(fs.getEmpId(),fs.getStustId(),fs.getStartTime(),fs.getEndTime(),fs.getType(),pageable);
				break;
			case 2:
				formRecords = frRepo.findOvertime(fs.getEmpId(),fs.getStustId(),fs.getStartTime(),fs.getEndTime(),fs.getType(),pageable);
				break;
			case 3:
				formRecords = frRepo.findResignation(fs.getEmpId(),fs.getStustId(),fs.getStartTime(),fs.getEndTime(),pageable);
				break;
			default:
				return null;
		}
		Page<FormDTO> formDTOS = formRecords.map(formRecord -> new FormDTO(formRecord));
		return new ResponseEntity<>(formDTOS,HttpStatus.OK);
	}

	@PutMapping("/form/revoke")
	public ResponseEntity<?> revokeForm(@RequestParam Integer empId, @RequestParam Integer formId){
		formService.revokeForm(empId,formId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/form/findRemainingLeaveDays")
	public ResponseEntity<?> findRemainingLeaveDays(@RequestParam Integer typeId,@RequestParam Integer empId){
		return new ResponseEntity<>(frRepo.getRemainingLeaveDays(typeId, LocalDate.now().getMonthValue(),empId),HttpStatus.OK);
	}

	@PostMapping("/test")
	public ResponseEntity<?>asd(@RequestBody FormSelect fs, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return new ResponseEntity<>(
				frRepo.findResignation(fs.getEmpId(),fs.getStustId(),fs.getStartTime(),fs.getEndTime(),pageable).map(formRecord -> new FormDTO(formRecord)), HttpStatus.OK);
	}

//	批審表單
	@PutMapping("/form/check")
	public ResponseEntity<?> checkEmpForm(@RequestParam Integer yn, @RequestBody FormAuditEventLog newauditLog) {
		formService.checkEmpForm(yn,newauditLog);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}