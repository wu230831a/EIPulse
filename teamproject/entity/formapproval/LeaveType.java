package com.eipulse.teamproject.entity.formapproval;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 請假種類(產假、婚假、事假....)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "leave_type")
public class LeaveType {

	@Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;
	
	// 該假別可請天數(如特休3天、生理假12天)
	@Column(name="days")
	private Integer days;
	
	// 請假種類(如特休、產假、生理假)
	@Column(name="type")
	private String type;
	
	// 用來控管此人可否此該類假別(ex.男性不能請生理假)
	// 預設為1(正常使用)、2(女性才可)
	@Column(name="status")
	private Integer status;
	
	@Column(name="remark")
	private String remark;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH},mappedBy="leaveType")
	@JsonManagedReference(value ="leaveType-leave")
	private List<Leave> leave;

	
}
