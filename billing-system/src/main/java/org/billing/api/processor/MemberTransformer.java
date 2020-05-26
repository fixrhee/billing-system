package org.billing.api.processor;

import org.billing.api.data.Member;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.http.internal.ParameterMap;
import org.mule.transformer.AbstractMessageTransformer;

public class MemberTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		ParameterMap map = (ParameterMap) message.getPayload();
		Member member = new Member();
		member.setUsername(map.get("msisdn"));
		member.setAddress(map.get("address"));
		member.setEmail(map.get("email"));
		member.setIdCard(map.get("idCard"));
		member.setMsisdn(map.get("msisdn"));
		member.setName(map.get("name"));
		member.setPassword(map.get("password"));
		return member;
	}

}
