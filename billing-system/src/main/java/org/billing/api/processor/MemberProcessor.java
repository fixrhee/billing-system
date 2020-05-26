package org.billing.api.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.billing.api.data.Group;
import org.billing.api.data.Member;
import org.billing.api.data.SessionToken;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class MemberProcessor {

	@Autowired
	private MemberRepository memberRepository;
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

}
