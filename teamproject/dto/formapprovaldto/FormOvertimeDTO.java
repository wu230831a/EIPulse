package com.eipulse.teamproject.dto.formapprovaldto;

import java.sql.Time;
import java.time.LocalDateTime;

import com.eipulse.teamproject.entity.formapproval.Overtime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormOvertimeDTO {

	private Integer formId;
	private Integer typeId;
	private String typeName;
	private String reason;
	private LocalDateTime date;
	private Time startTime;
	private Time endTime;
	private String file;
	
	public FormOvertimeDTO(Overtime overtime) {
		this.formId = overtime.getFormRecord().getFormId();
		this.typeId = overtime.getOvertimeType().getTypeId();
		this.typeName = overtime.getOvertimeType().getType();
		this.reason = overtime.getReason();
		this.date = overtime.getDate();
		this.startTime = overtime.getStartTime();
		this.endTime = overtime.getEndTime();
		this.file = overtime.getFile();
	}	
	
}
