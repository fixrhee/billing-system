package org.billing.api.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.billing.api.data.Billing;
import org.billing.api.data.Member;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillingProcessor {

	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private BillingRepository billingRepository;

	public Map<String, Object> getAllBilling(int currentPage, int pageSize, String token) throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Billing> lacq = billingRepository.getAllBilling(currentPage, pageSize, b.getId());
		Integer count = billingRepository.totalBilling(b.getId());
		mm.put("body", lacq);
		mm.put("totalRecord", count);
		return mm;
	}

	public Billing getBillingByID(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		return lacq;
	}

	public void createBilling(Billing billing, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		billingRepository.createBilling(billing, b.getId());
	}

	public void updateBilling(String id, Billing billing, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		billingRepository.updateBilling(billing, id);
	}

	public void deleteBilling(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		try {
			billingRepository.deleteBilling(id);
		} catch (Exception ex) {
			throw new TransactionException(Status.SERVICE_NOT_ALLOWED);
		}
	}
}
