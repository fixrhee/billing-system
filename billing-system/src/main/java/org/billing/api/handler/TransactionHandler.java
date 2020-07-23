package org.billing.api.handler;

import java.math.BigDecimal;
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

	public ServiceResponse getBalanceInquiry(String token, String accountID) {
		try {
			Map<String, Object> lacq = transactionProcessor.getBalanceInquiry(token, accountID);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse loadTransactionHistory(String start, String end, int currentPage, int pageSize,
			String token) {
		try {
			Map<String, Object> lacq = transactionProcessor.loadTransactionHistory(start, end, currentPage, pageSize,
					token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse doPayment(String fromUsername, String toUsername, String traceNo, String description,
			String refNo, BigDecimal amount, Integer trfTypeID, String trxState, String token) {
		try {
			Map<String, Object> lacq = transactionProcessor.doPayment(fromUsername, toUsername, traceNo, description,
					refNo, amount, trfTypeID, trxState, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
