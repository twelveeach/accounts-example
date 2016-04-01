package com.example.resources;

import java.math.BigDecimal;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.storage.DataStore;
import com.example.storage.IDataStoreTransaction;
import com.google.inject.Inject;

/**
 * The REST resource for an account.
 */
@Path(ResourceLocation.ACCOUNTS_LOCATION)
public class AccountResource {

	private final DataStore dataStore;

	/**
	 * Creates a new {@link AccountResource}.
	 * 
	 * @param dataStore
	 *            The {@link DataStore} to use for persistence.
	 */
	@Inject
	public AccountResource(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	/**
	 * Creates a new {@link Account}.
	 * 
	 * @return The new {@link Account}.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccount() {

		final String id = UUID.randomUUID().toString();
		final BigDecimal balance = BigDecimal.valueOf(0L, 2);
		final Account account = new Account(id, balance);
		
		dataStore.storeAccount(account);
		
		return Response.status(Status.CREATED).entity(account).build();
	}
	
	/**
	 * Returns the specified {@link Account}, or {@link Status#NOT_FOUND} if the
	 * {@link Account} does not exist.
	 * 
	 * @param accountId
	 *            The ID of the {@link Account}.
	 * @return See above.
	 */
	@GET
	@Path(ResourceLocation.ACCOUNT_ID_PARAM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccount(
			@PathParam(ResourceLocation.ACCOUNT_ID) String accountId) {
		
		final Account account = dataStore.retrieveAccount(accountId);
		
		return Response.status(Status.OK).entity(account).build();
	}
	
	/**
	 * Applies a {@link Deposit} to the specified {@link Account}, returning
	 * the updated {@link Account} or {@link Status#NOT_FOUND} if the
	 * {@link Account} does not exist.
	 * 
	 * @param accountId
	 *            The ID of the {@link Account}.
	 * @return See above.
	 */
	@POST
	@Path(ResourceLocation.ACCOUNT_DEPOSIT_LOCATION)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDeposit(
			@PathParam(ResourceLocation.ACCOUNT_ID) String accountId,
			Deposit deposit) {
		
		final IDataStoreTransaction dataStoreTransaction = (dataStore) -> { 
			final Account account = dataStore.retrieveAccount(accountId);
			account.setBalance(account.getBalance().add(deposit.getAmount()));
		};
		dataStore.executeTransaction(dataStoreTransaction);

		final Account account = dataStore.retrieveAccount(accountId);
		
		return Response.status(Status.CREATED).entity(account).build();
	}
	
	/**
	 * Applies a {@link Withdrawal} to the specified {@link Account}, returning
	 * the updated {@link Account} or {@link Status#NOT_FOUND} if the
	 * {@link Account} does not exist.
	 * 
	 * @param accountId
	 *            The ID of the {@link Account}.
	 * @return See above.
	 */
	@POST
	@Path(ResourceLocation.ACCOUNT_WITHDRAWAL_LOCATION)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createWithdrawal(
			@PathParam(ResourceLocation.ACCOUNT_ID) String accountId,
			Withdrawal withdrawal) {
		
		final IDataStoreTransaction dataStoreTransaction = (dataStore) -> { 
			final Account account = dataStore.retrieveAccount(accountId);
			account.setBalance(account.getBalance().subtract(withdrawal.getAmount()));
		};
		dataStore.executeTransaction(dataStoreTransaction);

		final Account account = dataStore.retrieveAccount(accountId);
		
		return Response.status(Status.CREATED).entity(account).build();
	}

	/**
	 * Applies a {@link Transfer} from the specified source {@link Account} to
	 * the target account specified in the {@link Transfer}, returning the
	 * updated {@link Account} or {@link Status#NOT_FOUND} if the either
	 * {@link Account} does not exist.
	 * 
	 * @param accountId
	 *            The ID of the source {@link Account}.
	 * @param transfer
	 *            The {@link Transfer}, containing the target {@link Account}
	 *            ID.
	 * @return See above.
	 */
	@POST
	@Path(ResourceLocation.ACCOUNT_TRANSFER_LOCATION)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTransfer(
			@PathParam(ResourceLocation.ACCOUNT_ID) String accountId,
			Transfer transfer) {
		
		final IDataStoreTransaction dataStoreTransaction = (dataStore) -> { 
			final Account sourceAccount = dataStore.retrieveAccount(accountId);
			final Account targetAccount = dataStore.retrieveAccount(transfer.getTargetAccountId());
			
			targetAccount.setBalance(targetAccount.getBalance().add(transfer.getAmount()));
			sourceAccount.setBalance(sourceAccount.getBalance().subtract(transfer.getAmount()));
		};
		dataStore.executeTransaction(dataStoreTransaction);

		final Account account = dataStore.retrieveAccount(accountId);
		
		return Response.status(Status.CREATED).entity(account).build();
	}
}
