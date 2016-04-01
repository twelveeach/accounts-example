package com.example.storage;

/**
 * An atomic change to execute against the {@link DataStore}.
 */
@FunctionalInterface
public interface IDataStoreTransaction {

	/**
	 * Apply the change to the {@link DataStore}.
	 * 
	 * @param datastore
	 *            The {@link DataStore} to apply the change to.
	 */
	void execute(DataStore datastore);
}
