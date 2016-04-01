package com.example.resources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

public class AccountResourceTest {

	private static final BigDecimal SEVENTY_FIVE = BigDecimal.valueOf(7500, 2);
	private static final BigDecimal TWENTY_FIVE = BigDecimal.valueOf(2500, 2);
	private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(10000, 2);
	private static final BigDecimal ONE_HUNDRED_TWENTY_FIVE = BigDecimal.valueOf(12500, 2);

	private static final String NON_EXISTENT_ACCOUNT_ID = "non-existent-account-id";
	
	private AccountExampleTestService testService;

	@Before
	public void setUp() {
		testService = new AccountExampleTestService();
	}
	
	@Test 
	public void createAccountThenCreatesNewEmptyAccount() {
		// Given, When
		final Response response = createAccount();
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));
		
		final Account createdAccount = response.readEntity(Account.class);
		assertThat(createdAccount.getId(), not(nullValue()));
		assertThat(createdAccount.getBalance(), equalTo(BigDecimal.valueOf(0, 2)));
	}

	@Test 
	public void getAccountGivenAccountDoesNotExistThenNotFound() {
		// Given, When
		final Response response = getAccount(NON_EXISTENT_ACCOUNT_ID);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
	}
	
	@Test 
	public void getAccountGivenAccountExistsThenReturnsAccount() {
		// Given
		final Account originalAccount = createAccount().readEntity(Account.class);
		
		// When
		final Response response = getAccount(originalAccount.getId());
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
		
		final Account account = response.readEntity(Account.class);
		assertThat(account.getId(), equalTo(originalAccount.getId()));
		assertThat(account.getBalance(), equalTo(originalAccount.getBalance()));
	}
	
	@Test 
	public void createDepositGivenAccountDoesNotExistThenNotFound() {
		// Given, When
		final Deposit deposit = new Deposit(ONE_HUNDRED);
		final Response response = createDeposit(NON_EXISTENT_ACCOUNT_ID, deposit);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
	}

	@Test 
	public void createDepositGivenAccountExistsThenUpdatesBalance() {
		// Given
		final Account originalAccount = createAccount().readEntity(Account.class);
		
		final Deposit deposit = new Deposit(ONE_HUNDRED);
		
		// When
		final Response response = createDeposit(originalAccount.getId(), deposit);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));
		
		final Account updatedAccount = response.readEntity(Account.class);
		assertThat(updatedAccount.getBalance(), equalTo(ONE_HUNDRED));
	}
	
	@Test 
	public void createWithdrawalGivenAccountDoesNotExistThenNotFound() {
		// Given, When
		final Withdrawal withdrawal = new Withdrawal(ONE_HUNDRED);
		final Response response = createWithdrawal(NON_EXISTENT_ACCOUNT_ID, withdrawal);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
	}

	@Test 
	public void createWithdrawalGivenAccountExistsThenUpdatesBalance() {
		// Given
		final Account originalAccount = createAccount().readEntity(Account.class);
		
		final Deposit deposit = new Deposit(ONE_HUNDRED);
		createDeposit(originalAccount.getId(), deposit);
		
		final Withdrawal withdrawal = new Withdrawal(TWENTY_FIVE);
		
		// When
		final Response response = createWithdrawal(originalAccount.getId(), withdrawal);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));
		
		final Account updatedAccount = response.readEntity(Account.class);
		assertThat(updatedAccount.getBalance(), equalTo(SEVENTY_FIVE));
	}

	@Test 
	public void createTransferGivenSourceAccountDoesNotExistThenNotFound() {
		// Given
		final Account targetAccount = createAccount().readEntity(Account.class);

		// When
		final Transfer transfer = new Transfer(targetAccount.getId(), ONE_HUNDRED);
		final Response response = createTransfer(NON_EXISTENT_ACCOUNT_ID, transfer);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
	}

	@Test 
	public void createTransferGivenTargetAccountDoesNotExistThenNotFound() {
		// Given
		final Account sourceAccount = createAccount().readEntity(Account.class);

		// When
		final Transfer transfer = new Transfer(NON_EXISTENT_ACCOUNT_ID, ONE_HUNDRED);
		final Response response = createTransfer(sourceAccount.getId(), transfer);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
	}
	
	@Test 
	public void createTransferGivenAccountExistsThenUpdatesBalance() {
		// Given
		final Account sourceAccount = createAccount().readEntity(Account.class);
		final Account targetAccount = createAccount().readEntity(Account.class);
		createDeposit(sourceAccount.getId(), new Deposit(ONE_HUNDRED));
		createDeposit(targetAccount.getId(), new Deposit(ONE_HUNDRED));
		
		// When
		final Transfer transfer = new Transfer(targetAccount.getId(), TWENTY_FIVE);
		final Response response = createTransfer(sourceAccount.getId(), transfer);
		
		// Then
		assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));
		
		final Account updatedSourceAccount = response.readEntity(Account.class);
		assertThat(updatedSourceAccount.getBalance(), equalTo(SEVENTY_FIVE));
		
		final Account updatedTargetAccount = getAccount(targetAccount.getId()).readEntity(Account.class);
		assertThat(updatedTargetAccount.getBalance(), equalTo(ONE_HUNDRED_TWENTY_FIVE));
	}

	private Response createAccount() {
		return testService.getAccounts()
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.post(null);
	}
	
	private Response getAccount(String accountId) {
		return testService.getAccounts()
				.path(accountId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
	}
	
	private Response createDeposit(String accountId, Deposit deposit) {
		return testService.getAccounts()
				.path(accountId)
				.path(ResourceLocation.ACCOUNT_DEPOSIT_PATH_ELEMENT)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(deposit));
	}

	private Response createWithdrawal(String accountId, Withdrawal withdrawal) {
		return testService.getAccounts()
				.path(accountId)
				.path(ResourceLocation.ACCOUNT_WITHDRAWAL_PATH_ELEMENT)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(withdrawal));
	}
	
	private Response createTransfer(String accountId, Transfer transfer) {
		return testService.getAccounts()
				.path(accountId)
				.path(ResourceLocation.ACCOUNT_TRANSFER_PATH_ELEMENT)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(transfer));
	}
}
