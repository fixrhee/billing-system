package org.billing.api.data;

public class TransactionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3248905936236043140L;

	public TransactionException(Status ex) {
		super(ex.toString());
	}

}
