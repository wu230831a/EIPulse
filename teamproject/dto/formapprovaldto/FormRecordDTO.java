package com.eipulse.teamproject.dto.formapprovaldto;
import com.eipulse.teamproject.entity.employee.Employee;
import com.eipulse.teamproject.entity.formapproval.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FormRecordDTO {
    private Integer formId;
    private Integer typeId;
    private int empId;
    private Integer statusId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime terminationDate;
    private List<FormEventLog> formEventLog;
    private FormType formType;
    private FormStatus formStatus;
    private Employee employee;
    private Overtime overtime;
    private Leave leaveTable;

    public FormRecordDTO(FormRecord formRecord) {
        this.formId = formRecord.getFormId();
        this.typeId = formRecord.getTypeId();
        this.empId = formRecord.getEmpId();
        this.statusId = formRecord.getStatusId();
        this.startDate = formRecord.getStartDate();
        this.endDate = formRecord.getEndDate();
        this.terminationDate = formRecord.getTerminationDate();
        this.formEventLog = formRecord.getFormEventLog();
        this.formType = formRecord.getFormType();
        this.formStatus = formRecord.getFormStatus();
        this.employee = formRecord.getEmployee();
        this.overtime = formRecord.getOvertime();
        this.leaveTable = formRecord.getLeaveTable();
    }

}
