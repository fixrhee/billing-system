package org.billing.api.handler;

import java.util.Map;
import org.billing.api.data.Member;
import org.billing.api.data.ServiceResponse;
import org.billing.api.data.SessionToken;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.processor.MemberProcessor;
import org.billing.api.processor.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberHandler {

	@Autowired
	private MemberProcessor memberProcessor;

	public ServiceResponse createJWTHMAC256(String username, String password) {
		try {
			SessionToken token = memberProcessor.createJWTHMAC256(username, password);
			return ResponseBuilder.getStatus(Status.PROCESSED, token);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getAllMember(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = memberProcessor.getAllMember(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getMemberByID(String id, String token) {
		try {
			Member lacq = memberProcessor.getMemberByID(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getMemberProfile(String token) {
		try {
			Member lacq = memberProcessor.getMemberProfile(token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createMember(Member member, String token) throws TransactionException {
		try {
			memberProcessor.createMember(member, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse updateMember(String id, Member member, String token) throws TransactionException {
		try {
			memberProcessor.updateMember(id, member, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteMember(String id, String token) throws TransactionException {
		try {
			memberProcessor.deleteMember(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}
}
