package com.example.resources;

/**
 * URL components for the resources.
 */
public class ResourceLocation {

	private ResourceLocation() {
	}
	
	public static final String ACCOUNTS_LOCATION = "accounts";
	public static final String ACCOUNT_ID = "accountId";
	public static final String ACCOUNT_ID_PARAM = "{" + ACCOUNT_ID + "}";
	
	public static final String ACCOUNT_DEPOSIT_PATH_ELEMENT = "deposit";
	public static final String ACCOUNT_DEPOSIT_LOCATION = ACCOUNT_ID_PARAM + "/" + ACCOUNT_DEPOSIT_PATH_ELEMENT;

	public static final String ACCOUNT_WITHDRAWAL_PATH_ELEMENT = "withdrawal";
	public static final String ACCOUNT_WITHDRAWAL_LOCATION = ACCOUNT_ID_PARAM + "/" + ACCOUNT_WITHDRAWAL_PATH_ELEMENT;

	public static final String ACCOUNT_TRANSFER_PATH_ELEMENT = "transfer";
	public static final String ACCOUNT_TRANSFER_LOCATION = ACCOUNT_ID_PARAM + "/" + ACCOUNT_TRANSFER_PATH_ELEMENT;
}
