package org.billing.api.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.billing.api.data.Billing;
import org.billing.api.data.ChildMenu;
import org.billing.api.data.Group;
import org.billing.api.data.Member;
import org.billing.api.data.ParentMenu;
import org.billing.api.data.SessionToken;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Multimap;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class MemberProcessor {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private BillingRepository billingRepository;
	@Autowired
	private HazelcastInstance hazelcastInstance;

	public Map<String, Object> getAllMember(int currentPage, int pageSize, String token) throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		List<Integer> ids = memberRepository.getAllMember(currentPage, pageSize, b.getId());
		List<Member> lMap = memberRepository.getMemberInList(ids);
		Integer count = memberRepository.totalMember(b.getId());
		mm.put("body", lMap);
		mm.put("totalRecord", count);
		return mm;
	}

	public Member getMemberByID(String id, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Integer mid = memberRepository.getMembershipID(id, b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		Member lacq = memberRepository.getMemberByID(id);
		return lacq;
	}

	public Member getMemberByUsername(String username, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Member m = memberRepository.getMemberByUsername(username);
		if (m == null) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		Integer mid = memberRepository.getMembershipID(m.getId(), b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		return m;
	}

	public void createMember(Member member, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Member lacq = memberRepository.getMemberByUsername(member.getUsername());
		if (lacq != null) {
			Integer id = memberRepository.getMembershipID(lacq.getId(), b.getId());
			if (id != 0) {
				throw new TransactionException(Status.MEMBER_ALREADY_REGISTERED);
			} else {
				IMap<String, String> mapLock = hazelcastInstance.getMap("MemberLock");
				mapLock.lock(lacq.getUsername());
				Integer sequence = memberRepository.countMembership(lacq.getId());
				if (sequence == 99) {
					mapLock.unlock(lacq.getUsername());
					throw new TransactionException(Status.MEMBER_LIMIT);
				}
				memberRepository.createMembership(lacq.getId(), b.getId(), sequence + 1);
				mapLock.unlock(lacq.getUsername());
			}
		} else {
			Group group = new Group();
			group.setId(3);
			member.setGroup(group);
			Integer cid = memberRepository.createMember(member);
			memberRepository.createMembership(cid, b.getId(), 1);
		}
	}

	public void createBulkMember(Member member, String bid) throws TransactionException {
		Member b = memberRepository.getMemberByID(bid);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Member lacq = memberRepository.getMemberByUsername(member.getUsername());
		if (lacq != null) {
			Integer id = memberRepository.getMembershipID(lacq.getId(), b.getId());
			if (id != 0) {
				throw new TransactionException(Status.MEMBER_ALREADY_REGISTERED);
			} else {
				IMap<String, String> mapLock = hazelcastInstance.getMap("MemberLock");
				mapLock.lock(lacq.getUsername());
				Integer sequence = memberRepository.countMembership(lacq.getId());
				if (sequence == 99) {
					mapLock.unlock(lacq.getUsername());
					throw new TransactionException(Status.MEMBER_LIMIT);
				}
				memberRepository.createMembership(lacq.getId(), b.getId(), sequence + 1);
				mapLock.unlock(lacq.getUsername());
			}
		} else {
			Group group = new Group();
			group.setId(3);
			member.setGroup(group);
			Integer cid = memberRepository.createMember(member);
			memberRepository.createMembership(cid, b.getId(), 1);
		}
	}

	public void updateMember(String id, Member member, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Integer mid = memberRepository.getMembershipID(id, b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		memberRepository.updateMember(id, member);
	}

	public void deleteMember(String id, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Integer mid = memberRepository.getMembershipID(id, b.getId());
		if (mid == 0) {
			throw new TransactionException(Status.MEMBER_NOT_FOUND);
		}
		memberRepository.deleteMembership(id, b.getId());
	}

	public SessionToken createJWTHMAC256(String username, String secret) throws TransactionException {
		SessionToken st = new SessionToken();
		Member member = memberRepository.validateAccess(username, secret);
		if (member == null) {
			throw new TransactionException(Status.ACCESS_DENIED);
		}
		try {
			String md5Hex = DigestUtils.md5Hex(secret);
			String token = JWT.create().withIssuer("Jatelindo").withSubject(username).sign(Algorithm.HMAC256(md5Hex));
			st.setToken(token);
			return st;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new TransactionException(Status.UNKNOWN_ERROR);
		}
	}

	public DecodedJWT verifyJWTHMAC256(String token, String secret) throws Exception {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer("Jatelindo").build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt;
	}

	public String decodeJWTHMAC256(String token) throws Exception {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getSubject();
	}

	public Member Authenticate(String token) {
		try {
			String username = decodeJWTHMAC256(token);
			Member member = memberRepository.getMemberByUsername(username);
			verifyJWTHMAC256(token, member.getPassword());
			return member;
		} catch (Exception e) {
			return null;
		}
	}

	public Member getMemberProfile(String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Member lacq = memberRepository.getMemberByID(b.getId());
		return lacq;
	}

	public Map<String, Object> getMemberMenu(String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}

		Map<String, Object> mm = new HashMap<String, Object>();
		List<ParentMenu> lp = memberRepository.getMenu(b.getGroup().getId());
		List<ParentMenu> copyLp = new ArrayList<>();
		List<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < lp.size(); i++) {
			ids.add(lp.get(i).getId());
		}

		Multimap<Integer, ChildMenu> mc = memberRepository.getMenuMultiMap(ids);
		for (int i = 0; i < lp.size(); i++) {
			ParentMenu pm = lp.get(i);
			pm.setChildMenu(mc.get(pm.getId()));
			copyLp.add(pm);
		}
		mm.put("menu", copyLp);
		mm.put("welcomeMenu", memberRepository.getWelcomeMenu(b.getGroup().getId()));
		return mm;
	}

	public Map<String, Object> getMemberByBilling(int currentPage, int pageSize, String id, String token, boolean avail)
			throws TransactionException {
		Map<String, Object> mm = new HashMap<String, Object>();
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(id, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}

		Integer count = memberRepository.totalMember(b.getId());
		List<Member> lMap = new LinkedList<Member>();
		if (avail == false) {
			List<Integer> ids = memberRepository.getMemberByBilling(currentPage, pageSize, lacq.getId(), b.getId());
			lMap = memberRepository.getMemberInList(ids);
			mm.put("totalRecord", count);
		} else {
			List<Member> memberBill = new LinkedList<Member>();
			List<Integer> idm = memberRepository.getAllMember(currentPage, pageSize, b.getId());
			List<Member> lm = memberRepository.getMemberInList(idm);
			Map<Integer, String> memberBilling = memberRepository.getAllMemberIDByBilling(lacq.getId(), b.getId());

			for (int i = 0; i < lm.size(); i++) {
				if (memberBilling.containsKey(lm.get(i).getId())) {
					lm.get(i).setDescription("UNAVAILABLE");
					memberBill.add(lm.get(i));
				} else {
					lm.get(i).setDescription("AVAILABLE");
					memberBill.add(lm.get(i));
				}
			}

			List<Member> sortedBill = memberBill.stream().sorted(Comparator.comparing(Member::getDescription))
					.collect(Collectors.toList());
			lMap = sortedBill;
			mm.put("totalRecord", count);
		}
		mm.put("body", lMap);
		return mm;
	}

	public Member searchMemberByBilling(String username, String billingID, String token) throws TransactionException {
		Member b = this.Authenticate(token);
		if (b == null) {
			throw new TransactionException(Status.UNAUTHORIZED_ACCESS);
		}
		Billing lacq = billingRepository.getBillingByID(billingID, b.getId());
		if (lacq == null) {
			throw new TransactionException(Status.BILLING_NOT_FOUND);
		}
		Member ma = memberRepository.getMemberByBilling(billingID, username, b.getId());
		if (ma == null) {
			Member mu = memberRepository.getMemberByUsername(username);
			if (mu == null) {
				throw new TransactionException(Status.MEMBER_NOT_FOUND);
			}
			Integer mid = memberRepository.getMembershipID(mu.getId(), b.getId());
			if (mid == 0) {
				throw new TransactionException(Status.MEMBER_NOT_FOUND);
			}
			mu.setDescription("AVALIABLE");
			return mu;
		} else {
			ma.setDescription("UNAVAILABLE");
			return ma;
		}
	}
}
