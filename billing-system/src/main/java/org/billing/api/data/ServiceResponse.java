package org.billing.api.data;

public class ServiceResponse {

	private String status;
	private String responseCode;
	private String description;
	private Object payload;
	private Integer totalRecord;

	public ServiceResponse() {

	}

	public ServiceResponse(String status, String code, String description, Object payload) {
		setStatus(status);
		setResponseCode(code);
		setDescription(description);
		setPayload(payload);
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

}
