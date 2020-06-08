package org.billing.api.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.billing.api.data.Billing;
import org.billing.api.data.Invoice;
import org.billing.api.data.Member;
import org.billing.api.data.PublishInvoice;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class InvoiceProcessor {

	@Autowired
	private BillingRepository billingRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private HazelcastInstance hazelcastInstance;

	public Map<String, Object> getAllInvoice(int currentPage, int pageSize, String token) throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Invoice> lacq = invoiceRepository.getAllInvoice(currentPage, pageSize, b.getId());
		if (lacq.size() == 0) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}

		List<Integer> ids = new LinkedList<Integer>();
		List<Integer> idb = new LinkedList<Integer>();
		List<Integer> idi = new LinkedList<Integer>();
		for (int i = 0; i < lacq.size(); i++) {
			ids.add(lacq.get(i).getMember().getId());
			idb.add(lacq.get(i).getBilling().getId());
			idi.add(lacq.get(i).getId());
		}

		List<Invoice> li = new ArrayList<Invoice>(lacq);
		Map<Integer, Member> pm = memberRepository.getMemberInMap(ids);
		Map<Integer, Billing> pb = billingRepository.getBillingInMap(idb);
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		Map<Integer, PublishInvoice> pi = invoiceRepository.getPublishInvoiceDateInMap(month, idi);
		for (int i = 0; i < lacq.size(); i++) {
			li.get(i).setMember(pm.get(lacq.get(i).getMember().getId()));
			li.get(i).setBilling(pb.get(lacq.get(i).getBilling().getId()));
			li.get(i).setPublishInvoice(pi.get(lacq.get(i).getId()));
		}
		Integer count = invoiceRepository.totalInvoice(b.getId());
		mm.put("body", li);
		mm.put("totalRecord", count);
		return mm;
	}

	public Invoice getInvoiceByID(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Invoice lacq = invoiceRepository.getInvoiceByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}
		Member m = memberRepository.getMemberByID(lacq.getMember().getId());
		PublishInvoice pi = invoiceRepository.getPublishInvoiceByInvoice(lacq.getId(), b.getId());
		lacq.setPublishInvoice(pi);
		lacq.setMember(m);
		return lacq;
	}

	public Invoice getInvoiceByNo(String no, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Invoice lacq = invoiceRepository.getInvoiceByNo(no, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}
		Member m = memberRepository.getMemberByID(lacq.getMember().getId());
		PublishInvoice pi = invoiceRepository.getPublishInvoiceByInvoice(lacq.getId(), b.getId());
		Billing bi = billingRepository.getBillingByID(lacq.getBilling().getId(), b.getId());
		lacq.setPublishInvoice(pi);
		lacq.setMember(m);
		lacq.setBilling(bi);
		return lacq;
	}

	public void createInvoice(Invoice inv, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(inv.getBilling().getId(), b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		Integer mid = memberRepository.getMembershipID(inv.getMember().getId(), b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}

		Member m = memberRepository.getMemberByID(inv.getMember().getId());
		IMap<String, String> mapLock = hazelcastInstance.getMap("MemberLock");
		mapLock.lock(String.valueOf(b.getId()) + String.valueOf(m.getId()));

		Integer memberInvoice = invoiceRepository.countMemberInvoice(b.getId(), m.getId()) + 1;
		if (memberInvoice == 99) {
			throw new TransactionException(Status.MEMBER_INVOICE_LIMIT);
		}
		Integer memberSequence = memberRepository.getMembershipSequence(m.getId(), b.getId());
		String invoiceNumber = StringUtils.leftPad(String.valueOf(memberSequence), 2, '0')
				+ StringUtils.leftPad(String.valueOf(memberInvoice), 2, '0')
				+ StringUtils.leftPad(m.getUsername().substring(4), 9, '0');
		inv.setInvoiceNumber(invoiceNumber);
		inv.setBiller(b);
		invoiceRepository.createInvoice(inv, b.getId());
		mapLock.unlock(String.valueOf(b.getId()) + String.valueOf(m.getId()));
	}

	public void createBatchInvoice(Invoice inv, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(inv.getBilling().getId(), b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		Integer mid = memberRepository.getMembershipID(inv.getMember().getId(), b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}

		Member m = memberRepository.getMemberByID(inv.getMember().getId());
		IMap<String, String> mapLock = hazelcastInstance.getMap("MemberLock");
		mapLock.lock(String.valueOf(b.getId()) + String.valueOf(m.getId()));

		Integer memberInvoice = invoiceRepository.countMemberInvoice(b.getId(), m.getId()) + 1;
		if (memberInvoice == 99) {
			throw new TransactionException(Status.MEMBER_INVOICE_LIMIT);
		}
		Integer memberSequence = memberRepository.getMembershipSequence(m.getId(), b.getId());
		String invoiceNumber = StringUtils.leftPad(String.valueOf(memberSequence), 2, '0')
				+ StringUtils.leftPad(String.valueOf(memberInvoice), 2, '0')
				+ StringUtils.leftPad(m.getUsername().substring(4), 9, '0');
		inv.setInvoiceNumber(invoiceNumber);
		inv.setBiller(b);
		invoiceRepository.createInvoice(inv, b.getId());
		mapLock.unlock(String.valueOf(b.getId()) + String.valueOf(m.getId()));
	}

	public void updateInvoice(String id, String amount, String active, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Invoice lacq = invoiceRepository.getInvoiceByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}
		invoiceRepository.updateInvoice(new BigDecimal(amount), Boolean.valueOf(active), id);
	}

	public void deleteInvoice(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Invoice lacq = invoiceRepository.getInvoiceByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}
		try {
			invoiceRepository.deleteInvoice(id);
		} catch (Exception ex) {
			throw new TransactionException(Status.SERVICE_NOT_ALLOWED);
		}
	}

	public Map<String, Object> getAllPublishInvoice(int currentPage, int pageSize, String token)
			throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<PublishInvoice> lacq = invoiceRepository.getAllPublishInvoice(currentPage, pageSize, b.getId());
		Integer count = invoiceRepository.totalPublishInvoice(b.getId());
		if (lacq.size() == 0) {
			throw new TransactionException(Status.INVOICE_NOT_FOUND);
		}
		List<Integer> ids = new LinkedList<Integer>();
		List<Integer> idb = new LinkedList<Integer>();
		for (int i = 0; i < lacq.size(); i++) {
			ids.add(lacq.get(i).getMember().getId());
			idb.add(lacq.get(i).getBilling().getId());
		}

		List<PublishInvoice> lpi = new ArrayList<PublishInvoice>(lacq);
		Map<Integer, Member> pm = memberRepository.getMemberInMap(ids);
		Map<Integer, Billing> bm = billingRepository.getBillingInMap(idb);

		for (int i = 0; i < lacq.size(); i++) {
			lpi.get(i).setMember(pm.get(lacq.get(i).getMember().getId()));
			lpi.get(i).setBilling(bm.get(lacq.get(i).getBilling().getId()));
		}

		mm.put("body", lpi);
		mm.put("totalRecord", count);
		return mm;
	}

	public List<Invoice> createBulkInvoice(String billingID, ArrayList<String> members, String amount, String description, String token)
			throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Invoice> li = new LinkedList<Invoice>();
		for (int i = 0; i < members.size(); i++) {
			Invoice inv = new Invoice();
			inv.setAmount(new BigDecimal(amount));
			Billing billing = new Billing();
			billing.setId(Integer.valueOf(billingID));
			inv.setBilling(billing);
			Member member = new Member();
			member.setId(Integer.valueOf(members.get(i)));
			inv.setMember(member);
			Member biller = new Member();
			biller.setId(b.getId());
			inv.setBiller(biller);
			inv.setDescription(description);
			li.add(inv);
		}
		return li;
	}
}
