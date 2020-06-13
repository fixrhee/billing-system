package org.billing.api.processor;

import org.billing.api.data.ServiceResponse;
import org.billing.api.data.Status;

public abstract class ResponseBuilder {

	public static ServiceResponse getStatus(String arg, Object payload) {
		return ResponseBuilder.getStatus(Status.valueOf(arg), payload);
	}

	public static ServiceResponse getStatus(Status arg, Object payload) {
		switch (arg) {
		case PROCESSED:
			return new ServiceResponse(arg.toString(), "200", "Transaction Succesfull", payload);
		case INVALID_PARAMETER:
			return new ServiceResponse(arg.toString(), "404", "Incomplete Request Parameter", payload);
		case MEMBER_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Member Not Found On System", payload);
		case BILLER_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Biller Not Found On System", payload);
		case BILLING_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Billing Not Found On System", payload);
		case INVOICE_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Invoice Not Found On System", payload);
		case MESSAGE_NOT_FOUND:
			return new ServiceResponse(arg.toString(), "404", "The Specified Message Not Found On System", payload);
		case MEMBER_ALREADY_REGISTERED:
			return new ServiceResponse(arg.toString(), "404", "The Specified Member Already Registered On System",
					payload);
		case MEMBER_INVOICE_LIMIT:
			return new ServiceResponse(arg.toString(), "429", "Member Invoice Limit Reached", payload);
		case MEMBER_LIMIT:
			return new ServiceResponse(arg.toString(), "429", "Member Sequence Limit Reached", payload);
		case UNAUTHORIZED_ACCESS:
			return new ServiceResponse(arg.toString(), "403", "Invalid Token Signature", payload);
		case ACCESS_DENIED:
			return new ServiceResponse(arg.toString(), "403", "You don't have Permission to Access this WebService",
					payload);
		case SERVICE_NOT_ALLOWED:
			return new ServiceResponse(arg.toString(), "403", "Method not Allowed", payload);
		case INVALID_DATE:
			return new ServiceResponse(arg.toString(), "403", "Invalid Date Format", payload);
		default:
			return new ServiceResponse("UNKNOWN_ERROR", "500", "Transaction Failed, Please Contact Administrator",
					payload);
		}
	}
}
