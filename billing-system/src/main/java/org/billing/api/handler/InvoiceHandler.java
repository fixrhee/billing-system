package org.billing.api.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.billing.api.data.Invoice;
import org.billing.api.data.ServiceResponse;
import org.billing.api.data.Status;
import org.billing.api.data.TransactionException;
import org.billing.api.processor.InvoiceProcessor;
import org.billing.api.processor.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceHandler {

	@Autowired
	private InvoiceProcessor invoiceProcessor;

	public ServiceResponse getAllInvoice(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = invoiceProcessor.getAllInvoice(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getAllPublishInvoice(int currentPage, int pageSize, String token) {
		try {
			Map<String, Object> lacq = invoiceProcessor.getAllPublishInvoice(currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse loadAllPublishInvoiceBillingStatus(String start, String end, String billingID, String status,
			int currentPage, int pageSize, String token) throws TransactionException {
		try {
			Map<String, Object> lacq = invoiceProcessor.loadAllPublishInvoiceBillingStatus(start, end, billingID,
					status, currentPage, pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse loadAllPublishInvoiceBilling(String start, String end, String billingID, int currentPage,
			int pageSize, String token) throws TransactionException {
		try {
			Map<String, Object> lacq = invoiceProcessor.loadAllPublishInvoiceBilling(start, end, billingID, currentPage,
					pageSize, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getInvoiceByID(String id, String token) {
		try {
			Invoice lacq = invoiceProcessor.getInvoiceByID(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse getInvoiceByNo(String no, String token) {
		try {
			Invoice lacq = invoiceProcessor.getInvoiceByNo(no, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, lacq);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse createInvoice(Invoice inv, String token) throws TransactionException {
		try {
			invoiceProcessor.createInvoice(inv, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public List<Invoice> createBulkInvoice(String billingID, ArrayList<String> members, String amount,
			String description, String token) throws TransactionException {
		try {

			return invoiceProcessor.createBulkInvoice(billingID, members, amount, description, token);
		} catch (TransactionException e) {
			return null;
		}
	}

	public ServiceResponse updateInvoice(String id, String amount, String active, String token)
			throws TransactionException {
		try {
			invoiceProcessor.updateInvoice(id, amount, active, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

	public ServiceResponse deleteInvoice(String id, String token) throws TransactionException {
		try {
			invoiceProcessor.deleteInvoice(id, token);
			return ResponseBuilder.getStatus(Status.PROCESSED, null);
		} catch (TransactionException e) {
			return ResponseBuilder.getStatus(e.getMessage(), null);
		}
	}

}
