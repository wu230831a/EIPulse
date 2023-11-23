package com.eipulse.teamproject.dto.formapprovaldto;

import java.time.LocalDateTime;

import com.eipulse.teamproject.entity.formapproval.Leave;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormLeaveDTO {

  private Integer formId;
  
  private Integer typeId;

  private String typeName;

  private String reason;

  private Integer days;

  private Integer hours;

  private LocalDateTime startTime;

  private String file;

	  public FormLeaveDTO(Leave leave) {
			
		this.formId = leave.getFormRecord().getFormId();
		this.typeId = leave.getLeaveType().getTypeId();
		this.typeName = leave.getLeaveType().getType();
		this.reason = leave.getReason();
		this.days = leave.getDays();
		this.hours = leave.getHours();
		this.startTime = leave.getStartTime();
		this.file = leave.getFile();
	  }

}