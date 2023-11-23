package com.eipulse.teamproject.dto.formapprovaldto;
import com.eipulse.teamproject.entity.formapproval.Resignation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FormResignDTO {

    private Integer formId;
    private String reason;
    private LocalDate leaveDate;
    private boolean quit;
    private boolean transferForm;
    private String file;
    public FormResignDTO(Resignation resignation) {
        this.formId = resignation.getFormRecord().getFormId();
        this.reason = resignation.getReason();
        this.leaveDate = resignation.getLeaveDate();
        this.quit = resignation.isQuit();
        this.transferForm = resignation.isTransferForm();
        this.file = resignation.getFile();
    }

}

