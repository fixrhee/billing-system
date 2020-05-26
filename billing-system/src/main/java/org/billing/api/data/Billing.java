package org.billing.api.data;

import java.util.Date;

public class Billing {

	private Integer id;
	private Member member;
	private String name;
	private String description;
	private Integer billingCycle;
	private Boolean outstanding;
	private Date createdDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBillingCycle() {
		return billingCycle;
	}

	public void setBillingCycle(Integer billingCycle) {
		this.billingCycle = billingCycle;
	}

	public Boolean getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Boolean outstanding) {
		this.outstanding = outstanding;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
