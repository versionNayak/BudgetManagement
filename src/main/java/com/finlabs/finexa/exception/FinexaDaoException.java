package com.finlabs.finexa.exception;

public class FinexaDaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String returnMessage = null;

	public FinexaDaoException() {
		super();

	}

	public FinexaDaoException(Throwable excp) {
		super(excp);

	}

	public FinexaDaoException(String message) {
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
