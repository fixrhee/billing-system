package org.billing.api.processor;

import java.util.HashMap;

import org.billing.api.data.Member;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class BulkMemberTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		HashMap<String,String> map = (HashMap<String,String>) message.getPayload();
		Member member = new Member();
		member.setUsername(map.get("msisdn"));
		member.setAddress(map.get("address"));
		member.setEmail(map.get("email"));
		member.setIdCard(map.get("idCard"));
		if (map.get("msisdn").startsWith("0")) {
			member.setMsisdn("62" + map.get("msisdn").substring(1));
		} else {
			member.setMsisdn(map.get("msisdn"));
		}
		member.setName(map.get("name"));
		member.setPassword(map.get("password"));
		return member;
	}

}
