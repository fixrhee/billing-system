package org.billing.api.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.billing.api.data.Journal;
import org.billing.api.data.Member;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessor {

	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private TransferTypeRepository transferTypeRepository;

	public Map<String, Object> getBalanceInquiry(String token) throws TransactionException {
		Map<String, Object> balanceMap = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		BigDecimal balance = transactionRepository.getBalanceInquiry(b.getId());
		BigDecimal pending = transactionRepository.getPendingBalance(b.getId());
		if (balance == null) {
			balance = BigDecimal.ZERO;
		}
		if (pending == null) {
			pending = BigDecimal.ZERO;
		}
		balanceMap.put("balance", balance);
		balanceMap.put("pendingAmount", pending);
		return balanceMap;
	}

	public Map<String, Object> loadTransactionHistory(int pageSize, int rowNum, String token)
			throws TransactionException {
		Map<String, Object> hm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Journal> tl = transactionRepository.loadTransactionHistory(pageSize, rowNum, b.getId());
		List<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < tl.size(); i++) {
			ids.add(tl.get(i).getTransferType().getId());
		}
		List<Journal> lj = new ArrayList<Journal>(tl);
		Map<Integer, TransferType> pm = transferTypeRepository.getTransferTypeInMap(ids);
		for (int i = 0; i < tl.size(); i++) {
			lj.get(i).setTransferType(pm.get(tl.get(i).getTransferType().getId()));
		}
		Integer count = transactionRepository.totalTransaction(b.getId());
		hm.put("body", lj);
		hm.put("totalRecord", count);
		return hm;
	}

}
