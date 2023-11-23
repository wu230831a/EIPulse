package com.eipulse.teamproject.entity.formapproval;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class FormSelect {
    private Integer formType;
    private Integer type;
    private Integer empId;
    private Integer stustId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
