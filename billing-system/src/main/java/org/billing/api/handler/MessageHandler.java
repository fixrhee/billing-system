package org.billing.api.handler;

import java.util.Map;

import org.billing.api.data.Message;
import org.billing.api.data.ServiceResponse;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.processor.MessageProcessor;
import org.billing.api.processor.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {

	@Autowired
	private MessageProcessor messageProcessor;

	public ServiceResponse getAllMessage(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = messageProcessor.getAllMessage(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getMessageByID(String id, String token) {
		try {
			Message lacq = messageProcessor.getMessageByID(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse readMessage(String id, String token) {
		try {
			messageProcessor.readMessage(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createMessage(String username, String subject, String body, String token)
			throws TransactionException {
		try {
			messageProcessor.createMessage(username, subject, body, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteMessage(String id, String token) throws TransactionException {
		try {
			messageProcessor.deleteMessage(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}
}
