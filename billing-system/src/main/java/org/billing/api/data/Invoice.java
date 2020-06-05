package org.billing.api.data;

import java.math.BigDecimal;
import java.util.Date;

public class Invoice {

	private Integer id;
	private Billing billing;
	private Member biller;
	private Member member;
	private PublishInvoice publishInvoice;
	private String invoiceNumber;
	private BigDecimal amount;
	private Boolean active;
	private Date createdDate;

	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Member getBiller() {
		return biller;
	}

	public void setBiller(Member biller) {
		this.biller = biller;
	}

	public PublishInvoice getPublishInvoice() {
		return publishInvoice;
	}

	public void setPublishInvoice(PublishInvoice publishInvoice) {
		this.publishInvoice = publishInvoice;
	}

}
