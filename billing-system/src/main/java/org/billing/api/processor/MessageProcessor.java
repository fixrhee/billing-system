package org.billing.api.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.billing.api.data.Billing;
import org.billing.api.data.Member;
import org.billing.api.data.Message;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {

	@Autowired
	private MemberProcessor memberProcessor;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MessageRepository messageRepository;

	public Map<String, Object> getAllMessage(int currentPage, int pageSize, String token) throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Message> lacq = messageRepository.getAllMessage(currentPage, pageSize, b.getId());
		List<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < lacq.size(); i++) {
			ids.add(lacq.get(i).getFromMember().getId());
			ids.add(lacq.get(i).getToMember().getId());
		}

		Map<Integer, Member> pm = memberRepository.getMemberInMap(ids);
		List<Message> lm = new LinkedList<Message>(lacq);
		for (int i = 0; i < lacq.size(); i++) {
			lm.get(i).setFromMember(pm.get(lacq.get(i).getFromMember().getId()));
			lm.get(i).setToMember(pm.get(lacq.get(i).getToMember().getId()));
		}
		Integer count = messageRepository.totalMessage(b.getId());
		Integer unread = messageRepository.totalUnreadMessage(b.getId());
		mm.put("body", lm);
		mm.put("totalRecord", count);
		mm.put("unreadMessage", unread);
		return mm;
	}

	public Message getMessageByID(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Message lacq = messageRepository.getMessageByID(b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.MESSAGE_NOT_FOUND);
		}
		return lacq;
	}

	public void createMessage(String username, String subject, String body, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Member m = memberRepository.getMemberByUsername(username);
		if (m == null) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		messageRepository.createMessage(b.getId(), m.getId(), subject, body);
	}

	public void readMessage(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Message msg = messageRepository.getMessageByID(id);
		if (msg == null) {
			throw new TransactionException(Status.MESSAGE_NOT_FOUND);
		}
		messageRepository.readMessage(id);
	}

	public Map<String, Object> getUnreadMessage(String token) throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Integer count = messageRepository.totalUnreadMessage(b.getId());
		mm.put("unread", count);
		return mm;
	}

	public void deleteMessage(String id, String token) throws TransactionException {
		Member b = memberProcessor.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Message msg = messageRepository.getMessageByID(id);
		if (msg == null) {
			throw new TransactionException(Status.MESSAGE_NOT_FOUND);
		}
		messageRepository.deleteMessage(id);
	}
}
