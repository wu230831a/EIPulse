package com.eipulse.teamproject.entity.formapproval;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.sql.Time;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

// 加班申請單

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="apply_overtime")
public class Overtime {

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id", referencedColumnName = "form_id")
	private FormRecord formRecord;

	// 加班種類(平日加班、假日加班、國定假日加班)
	@JsonBackReference(value ="overtime-type")
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name="type",nullable = false,insertable = false,updatable = false)
	private OvertimeType overtimeType;

	@Column(name="type")
	private Integer typeId;

	@Column(name = "reason")
	private String reason;

	@Column(name="date")
	private java.sql.Date date;

	@Column(name = "start_time")
	private Time startTime;

	@Column(name = "end_time")
	private Time endTime;

	@Column(name="file")
	private String file;

}
