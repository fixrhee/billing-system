package org.billing.api.handler;

import java.util.Map;

import org.billing.api.data.ServiceResponse;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.processor.ResponseBuilder;
import org.billing.api.processor.TransactionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionHandler {

	@Autowired
	private TransactionProcessor transactionProcessor;

	public ServiceResponse getBalanceInquiry(String token) {
		try {
			Map<String, Object> lacq = transactionProcessor.getBalanceInquiry(token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse loadTransactionHistory(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = transactionProcessor.loadTransactionHistory(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
