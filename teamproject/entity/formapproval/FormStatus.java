package com.eipulse.teamproject.entity.formapproval;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "form_status")
public class FormStatus {
	
	@Id
	@Column(name = "Status_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer statusId;
	
	@Column(name = "Description")
	private String description;

}
