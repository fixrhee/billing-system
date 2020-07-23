package org.billing.api.data;

import java.util.Date;

public class Account {

	private Integer id;
	private String name;
	private Boolean systemAccount;
	private Date createdDate;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean isSystemAccount() {
		return systemAccount;
	}

	public void setSystemAccount(Boolean systemAccount) {
		this.systemAccount = systemAccount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
