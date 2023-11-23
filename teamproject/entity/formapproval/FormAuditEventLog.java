package com.eipulse.teamproject.entity.formapproval;

import java.time.LocalDateTime;
import com.eipulse.teamproject.entity.employee.Employee;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Entity
@Table(name = "form_audit_eventlog")
public class FormAuditEventLog {

	@Id
	@Column(name = "Event_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eventId;
	@Column(name = "Auditor")
	private int auditor;
	@Column(name = "Status_id")
	private int StatusId;
	@Column(name = "Message")
	private String message;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="End_date")
	private LocalDateTime endDate;


	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EventLog_id")
	private FormEventLog formEventLog;


	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Auditor",insertable = false, updatable = false)
	private Employee employee;


	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Status_id",insertable = false, updatable = false)
    private FormStatus formStatus;
}
