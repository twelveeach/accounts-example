package com.example.storage;

import java.util.Map;

import javax.ws.rs.NotFoundException;

import com.example.resources.Account;
import com.google.common.collect.Maps;

/**
 * A simple in-memory data store.
 */
public class DataStore {

	final Map<String, Account> accounts;

	/**
	 * Constructs a new {@link DataStore}.
	 */
	public DataStore() {
		accounts = Maps.newHashMap();
	}
	
	/**
	 * Returns the {@link Account} with the specified ID.
	 * 
	 * @param accountId
	 *            The ID of the {@link Account}.
	 * @return See above.
	 * @throws NotFoundException
	 *             if no {@link Account} with the specified ID can be retrieved.
	 */
	public Account retrieveAccount(String accountId) {
		final Account account = accounts.get(accountId);
		if (account == null) {
			throw new NotFoundException();
		}
		return account;
	}

	/**
	 * Stores the {@link Account}.
	 * 
	 * @param account
	 *            The {@link Account} to store.
	 */
	public void storeAccount(Account account) {
		accounts.put(account.getId(), account);
	}

	/**
	 * Executes the transaction against the {@link DataStore}.
	 * 
	 * @param transaction
	 *            The {@link IDataStoreTransaction} to execute.
	 */
	public synchronized void executeTransaction(IDataStoreTransaction transaction) {
		transaction.execute(this);
	}
}
