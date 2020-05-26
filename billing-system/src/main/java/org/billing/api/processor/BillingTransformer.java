package org.billing.api.processor;

import org.billing.api.data.Billing;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.http.internal.ParameterMap;
import org.mule.transformer.AbstractMessageTransformer;

public class BillingTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		ParameterMap map = (ParameterMap) message.getPayload();
		Billing billing = new Billing();
		billing.setBillingCycle(Integer.valueOf(map.get("billingCycle")));
		billing.setDescription(map.get("description"));
		billing.setName(map.get("name"));
		billing.setOutstanding(Boolean.valueOf(map.get("outstanding")));
		return billing;
	}

}
