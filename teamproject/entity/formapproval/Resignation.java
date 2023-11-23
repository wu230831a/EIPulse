package com.eipulse.teamproject.entity.formapproval;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "apply_resignation")
public class Resignation {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "form_id")
    private FormRecord formRecord;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(name="quit")
    private boolean quit;

    @Column(name="transfer_form")
    private boolean transferForm;

    @Column(name="file")
    private String file;

}