package com.example.resources;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Transport class for a withdrawal.
 */
public class Withdrawal implements Serializable {

	private static final long serialVersionUID = 3247023179704913777L;

	private BigDecimal amount;

	/**
	 * No-arg constructor required by Jackson.
	 */
	public Withdrawal() {
	}
	
	/**
	 * Creates a new {@link Withdrawal}.
	 * 
	 * @param amount
	 *            The amount to withdraw.
	 */
	public Withdrawal(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Returns the amount to withdraw.
	 * 
	 * @return See above.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
