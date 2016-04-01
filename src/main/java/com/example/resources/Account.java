package com.example.resources;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Transport class for an account.
 */
public class Account implements Serializable {

	private static final long serialVersionUID = 9033636704743745737L;

	private String id;

	private BigDecimal balance;

	/**
	 * No-arg constructor required by Jackson.
	 */
	public Account() {
	}
	
	/**
	 * Creates a new {@link Account}.
	 * 
	 * @param id
	 *            The ID of the {@link Account}.
	 * @param balance
	 *            The balance of the {@link Account}.
	 */
	public Account(String id, BigDecimal balance) {
		this.id = id;
		this.balance = balance;
	}

	/**
	 * Returns the ID of the {@link Account}.
	 * 
	 * @return See above.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the balance of the {@link Account}.
	 * 
	 * @return See above.
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Sets the balance of the {@link Account}.
	 * 
	 * @param balance
	 *            The balance to set.
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
