package com.eipulse.teamproject.entity.formapproval;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// 請假申請單


@Getter
@Setter
@Entity
@Table(name = "apply_leave")
public class Leave {

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id", referencedColumnName = "form_id")
	private FormRecord formRecord;


	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name = "type",nullable = false,insertable = false,updatable = false)
	@JsonBackReference(value = "leaveType-leave")
	private LeaveType leaveType;

    @Column(name="type")
    private Integer typeId;

	@Column(name="reason")
	private String reason;

	@Column(name="days")
	private Integer days;

	@Column(name="hours")
	private Integer hours;

	@Column(name="start_time")
	private LocalDateTime startTime;

	@Column(name="file")
	private String file;

}