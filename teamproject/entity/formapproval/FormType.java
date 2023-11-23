package com.eipulse.teamproject.entity.formapproval;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Entity
@Table(name = "form_type")
public class FormType {

	@Id
	@Column(name = "Type_id")
	private int typeId;
	@Column(name = "Type_name")
	private String typeName;

}
