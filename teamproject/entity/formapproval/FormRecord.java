package com.eipulse.teamproject.entity.formapproval;

import java.time.LocalDateTime;
import java.util.List;

import com.eipulse.teamproject.entity.employee.Employee;
import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Entity
@Table(name="form_record")
public class FormRecord {

	@Id
	@Column(name="Form_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer formId;
	@Column(name="Type_id")
	private Integer typeId;
	@Column(name="Emp_id")
	private Integer empId;
	@Column(name = "Status_id")
	private Integer statusId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Start_date")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="End_date")
	private LocalDateTime endDate;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Termination_date")
	private LocalDateTime terminationDate;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "formRecord", cascade = CascadeType.ALL)
    private List<FormEventLog> formEventLog;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Type_id",insertable = false, updatable = false)
	private FormType formType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Status_id",insertable = false, updatable = false)
    private FormStatus formStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Emp_id",insertable = false, updatable = false)
	private Employee employee;

	@OneToOne(fetch = FetchType.LAZY,mappedBy = "formRecord", cascade = CascadeType.ALL)
	private Overtime overtime;

	@OneToOne(fetch = FetchType.LAZY,mappedBy = "formRecord", cascade = CascadeType.ALL)
	private Leave leaveTable;

	@OneToOne(fetch = FetchType.LAZY,mappedBy = "formRecord", cascade = CascadeType.ALL)
	private Resignation resignation;
	
	@PrePersist // 當物件主換成 Persistent 狀態以前，做以下事情
	public void noCreate() {
		if (startDate == null) {
			startDate = LocalDateTime.now();
			terminationDate = startDate.plusDays(7);
		}
	}

}
