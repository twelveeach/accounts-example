package com.example.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;

public class AccountExampleTestService {

	private final Client client;

	public AccountExampleTestService() {
		client = ClientBuilder.newClient(new ClientConfig());
		client.register(JacksonJsonProvider.class);
	}
	
	public WebTarget getAccounts() {
		return client.target("http://localhost:8080")
				.path(ResourceLocation.ACCOUNTS_LOCATION);
	}
}
