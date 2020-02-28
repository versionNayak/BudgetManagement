package com.finlabs.finexa.exception;

public class FinexaServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String returnMessage = null;

	public FinexaServiceException() {
		super();

	}

	public FinexaServiceException(Throwable excp) {
		super(excp);

	}

	public FinexaServiceException(Throwable excp, String message) {
		super(excp);
		this.returnMessage = message;

	}

	public FinexaServiceException(String message) {
		super(message);
		this.returnMessage = message;

	}

	@Override
	public String getMessage() {
		return returnMessage;

	}

	public String toString() {
		return returnMessage;

	}
}
