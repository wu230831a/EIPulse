package com.eipulse.teamproject.entity.formapproval;

import java.time.LocalDateTime;
import java.util.List;

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
import lombok.Data;
@Data
@Entity
@Table(name = "form_eventlog")
public class FormEventLog {

	@Id
	@Column(name = "Event_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	@Column(name = "Sequence")
	private Integer sequence;
	@Column(name = "Status_id")
	private Integer StatusId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Start_date")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="End_date")
	private LocalDateTime endDate;
	@Column(name="Head_count")
	private Integer headCount;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "formEventLog", cascade = CascadeType.ALL)
    private List<FormAuditEventLog> formAuditEventLog;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Form_id")
	private FormRecord formRecord;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Status_id",insertable = false, updatable = false)
    private FormStatus formStatus;
	
	@PrePersist // 當物件主換成 Persistent 狀態以前，做以下事情
	public void noCreate() {
		if (startDate == null)
			startDate = LocalDateTime.now();
	}

}
