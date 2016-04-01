package com.example.resources;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Transport class for a transfer.
 */
public class Transfer implements Serializable {

	private static final long serialVersionUID = 2035615824451426239L;
	
	private String targetAccountId;
	private BigDecimal amount;

	/**
	 * No-arg constructor required by Jackson.
	 */
	public Transfer() {
	}
	
	/**
	 * Creates a new {@link Transfer}.
	 * 
	 * @param targetAccountId
	 *            The ID of the target {@link Account}.
	 * @param amount
	 *            The amount to transfer.
	 */
	public Transfer(String targetAccountId, BigDecimal amount) {
		this.targetAccountId = targetAccountId;
		this.amount = amount;
	}

	/**
	 * Returns the ID of the target {@link Account}.
	 * 
	 * @return See above.
	 */
	public String getTargetAccountId() {
		return targetAccountId;
	}

	/**
	 * Returns the amount to transfer.
	 * 
	 * @return See above.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}

