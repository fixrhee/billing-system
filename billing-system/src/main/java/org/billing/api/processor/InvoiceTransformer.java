package org.billing.api.processor;

import java.math.BigDecimal;

import org.billing.api.data.Billing;
import org.billing.api.data.Invoice;
import org.billing.api.data.Member;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.http.internal.ParameterMap;
import org.mule.transformer.AbstractMessageTransformer;

public class InvoiceTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		ParameterMap map = (ParameterMap) message.getPayload();
		Invoice inv = new Invoice();
		inv.setAmount(new BigDecimal(map.get("amount")));
		Billing billing = new Billing();
		billing.setId(Integer.valueOf(map.get("billingID")));
		inv.setBilling(billing);
		Member member = new Member();
		member.setId(Integer.valueOf(map.get("memberID")));
		inv.setMember(member);
		return inv;
	}

}
