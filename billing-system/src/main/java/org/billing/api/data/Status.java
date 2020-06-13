package org.billing.api.data;

public enum Status {
	PROCESSED, 
	UNAUTHORIZED_ACCESS, 
	SERVICE_NOT_ALLOWED, 
	ACCESS_DENIED, 
	VALID, 
	INVALID, 
	BLOCKED,
	INVALID_PARAMETER,
	MEMBER_NOT_FOUND,
	MEMBER_LIMIT,
	BILLER_NOT_FOUND,
	BILLING_NOT_FOUND,
	INVOICE_NOT_FOUND,
	CREDENTIAL_INVALID, 
	REQUEST_TIMEOUT,
	HOST_UNAVAILABLE,
	INVALID_AMOUNT,
	MEMBER_INVOICE_LIMIT,
	UNKNOWN_ERROR,
	MEMBER_ALREADY_REGISTERED,
	NO_TRANSACTION,
	TRANSACTION_BLOCKED,
	INVALID_DATE,
	MESSAGE_NOT_FOUND;
}
