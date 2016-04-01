package com.example.resources;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Transport class for a deposit.
 */
public class Deposit implements Serializable {

	private static final long serialVersionUID = -8751806096098589148L;

	private BigDecimal amount;

	/** 
	 * No-arg constructor required by Jackson.
	 */
	public Deposit() {
	}
	
	/**
	 * Creates a new {@link Deposit}.
	 * 
	 * @param amount
	 *            The amount to deposit.
	 */
	public Deposit(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Returns the amount.
	 * @return See above.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
