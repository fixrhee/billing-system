package org.billing.api.handler;

import java.util.Map;

import org.billing.api.data.Billing;
import org.billing.api.data.ServiceResponse;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.processor.BillingProcessor;
import org.billing.api.processor.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillingHandler {

	@Autowired
	private BillingProcessor billingProcessor;

	public ServiceResponse getAllBilling(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = billingProcessor.getAllBilling(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getBillingByID(String id, String token) {
		try {
			Billing lacq = billingProcessor.getBillingByID(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createBilling(Billing billing, String token) throws TransactionException {
		try {
			billingProcessor.createBilling(billing, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateBilling(String id, Billing billing, String token) throws TransactionException {
		try {
			billingProcessor.updateBilling(id, billing, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteBilling(String id, String token) throws TransactionException {
		try {
			billingProcessor.deleteBilling(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}
}
