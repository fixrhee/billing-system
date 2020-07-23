package org.billing.api.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.billing.api.data.Account;
import org.billing.api.data.Journal;
import org.billing.api.data.Member;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class TransactionProcessor {

	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private TransferTypeRepository transferTypeRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private HazelcastInstance hazelcastInstance;
	private Logger logger = Logger.getLogger(this.getClass());

	public Map<String, Object> getBalanceInquiry(String token, String accountID) throws TransactionException {
		Map<String, Object> balanceMap = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Account facc = accountRepository.getAccountByID(accountID);
		if (facc == null) {
			logger.warn("[Account Not Found : " + accountID + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		Integer faccp = accountRepository.validateAccountPermission(accountID, b.getGroup().getId());
		if (faccp == 0) {
			logger.warn(
					"[Account Permission Not Found : " + accountID + " For Group ID : " + b.getGroup().getId() + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		BigDecimal balance = transactionRepository.getBalanceInquiry(b.getId(), accountID);
		BigDecimal pending = transactionRepository.getReservedAmount(b.getId(), accountID);
		if (balance == null) {
			balance = BigDecimal.ZERO;
		}
		if (pending == null) {
			pending = BigDecimal.ZERO;
		}
		balanceMap.put("balance", balance);
		balanceMap.put("account", facc);
		balanceMap.put("reservedAmount", pending);
		balanceMap.put("formattedBalance", Utils.formatAmount(balance.intValue()));
		balanceMap.put("formattedReservedAmount", Utils.formatAmount(pending.intValue()));
		return balanceMap;
	}

	public Map<String, Object> loadTransactionHistory(String start, String end, int pageSize, int rowNum, String token)
			throws TransactionException {
		Map<String, Object> hm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Journal> tl = transactionRepository.loadTransactionHistory(start, end, pageSize, rowNum, b.getId());
		if (tl.size() == 0) {
			throw new TransactionException(Status.NO_TRANSACTION);
		}
		List<Integer> tfTypeIds = new LinkedList<Integer>();
		List<Integer> accountIds = new LinkedList<Integer>();
		List<Integer> memberIds = new LinkedList<Integer>();
		for (int i = 0; i < tl.size(); i++) {
			tfTypeIds.add(tl.get(i).getTransferType().getId());
			accountIds.add(tl.get(i).getFromAccount().getId());
			accountIds.add(tl.get(i).getToAccount().getId());
			memberIds.add(tl.get(i).getFromMember().getId());
			memberIds.add(tl.get(i).getToMember().getId());
		}
		List<Journal> lj = new ArrayList<Journal>(tl);
		Map<Integer, TransferType> pm = transferTypeRepository.getTransferTypeInMap(tfTypeIds);
		Map<Integer, Account> am = accountRepository.getAccountInMap(accountIds);
		Map<Integer, Member> mm = memberRepository.getMemberInMap(memberIds);

		for (int i = 0; i < tl.size(); i++) {
			lj.get(i).setTransferType(pm.get(tl.get(i).getTransferType().getId()));
			lj.get(i).setFromAccount(am.get(tl.get(i).getFromAccount().getId()));
			lj.get(i).setToAccount(am.get(tl.get(i).getToAccount().getId()));
			lj.get(i).setFromMember(mm.get(tl.get(i).getFromMember().getId()));
			lj.get(i).setToMember(mm.get(tl.get(i).getToMember().getId()));
		}
		Integer count = transactionRepository.totalTransaction(b.getId());
		hm.put("body", lj);
		hm.put("totalRecord", count);
		return hm;
	}

	@Transactional
	public Map<String, Object> doPayment(String fromUsername, String toUsername, String traceNo, String description,
			String refNo, BigDecimal amount, int trxTypeID, String trxState, String token) throws TransactionException {

		Map<String, Object> hm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		// Validate From Username
		Member mf = memberProcessor.getMemberByUsername(fromUsername);
		if (mf == null) {
			logger.warn("[FROM Member Not Found : " + fromUsername + "]");
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}

		// Validate To Username
		Member mt = memberProcessor.getMemberByUsername(toUsername);
		if (mt == null) {
			logger.warn("[TO Member Not Found : " + toUsername + "]");
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}

		// Validate Transfer Type
		TransferType trfType = transferTypeRepository.getTrfTypeByID(trxTypeID);
		if (trfType == null) {
			logger.warn("[TransferType Not Found : " + trxTypeID + "]");
			throw new TransactionException(Status.INVALID_TRANSFER_TYPE);
		}

		Integer permissionID = transferTypeRepository.validateTrfTypePermission(trfType.getId(), mf.getGroup().getId());
		if (permissionID == 0) {
			logger.warn("[TransferType Permission Not Found : " + trxTypeID + " For Group : " + mf.getGroup().getName()
					+ "]");
			throw new TransactionException(Status.INVALID_TRANSFER_TYPE);
		}

		// Validate From Account
		Account facc = accountRepository.getAccountByID(trfType.getFromAccountID());
		if (facc == null) {
			logger.warn("[FROM Account Not Found : " + trfType.getFromAccountID() + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		Integer faccp = accountRepository.validateAccountPermission(trfType.getFromAccountID(), mf.getGroup().getId());
		if (faccp == 0) {
			logger.warn("[FROM Account Permission Not Found : " + trfType.getFromAccountID() + " For Group ID : "
					+ mf.getGroup().getId() + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		// Validate To Account
		Account tacc = accountRepository.getAccountByID(trfType.getToAccountID());
		if (tacc == null) {
			logger.warn("[TO Account Not Found : " + trfType.getToAccountID() + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		Integer taccp = accountRepository.validateAccountPermission(trfType.getToAccountID(), mt.getGroup().getId());
		if (taccp == 0) {
			logger.warn("[TO Account Permission Not Found : " + trfType.getToAccountID() + " For Group ID : "
					+ mt.getGroup().getId() + "]");
			throw new TransactionException(Status.INVALID_ACCOUNT);
		}

		Integer id = transactionRepository.validateTraceNo(traceNo);
		if (id != 0) {
			logger.warn("[" + fromUsername + "][TRACE Number Already USED : " + traceNo + "]");
			throw new TransactionException(Status.ALREADY_PAID);
		}

		// Lock Account and Check Balance
		IMap<String, String> mapLock = hazelcastInstance.getMap("AccountLock");
		mapLock.lock(mf.getUsername() + trfType.getFromAccountID());
		mapLock.lock(mt.getUsername() + trfType.getToAccountID());

		if (facc.isSystemAccount() == false) {
			BigDecimal balance = transactionRepository.getBalanceInquiry(mf.getId(), trfType.getFromAccountID());
			if (balance.compareTo(amount) < 0) {
				logger.warn("[" + fromUsername + "][INSUFFICIENT Balance : " + balance + " Amount : " + amount + "]");
				mapLock.lock(mf.getUsername() + trfType.getFromAccountID());
				mapLock.lock(mt.getUsername() + trfType.getToAccountID());
				throw new TransactionException(Status.INSUFFICIENT_BALANCE);
			}
		}

		// Create Payment and Unlock
		String referenceNo = refNo.equalsIgnoreCase("NA") ? null : refNo;
		String desc = description.equalsIgnoreCase("NA") ? trfType.getName() : description;
		String trxNo = Utils.getTransactionNumber();
		Integer tid = transactionRepository.createPayment(traceNo, desc, referenceNo, amount, trfType.getId(),
				trfType.getFromAccountID(), trfType.getToAccountID(), mf.getId(), mt.getId(), trxNo, trxState);
		mapLock.unlock(mf.getUsername() + trfType.getFromAccountID());
		mapLock.lock(mt.getUsername() + trfType.getToAccountID());
		hm.put("id", tid);
		hm.put("fromMember", mf);
		hm.put("toMember", mt);
		hm.put("fromAccount", facc);
		hm.put("toAccount", tacc);
		hm.put("transferType", trfType);
		hm.put("amount", amount);
		hm.put("traceNumber", traceNo);
		hm.put("transactionNumber", trxNo);
		hm.put("description", desc);
		return hm;
	}
}
