package com.example.resources;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * The root application required to configure Jersey.
 */
@ApplicationPath("/")
public class AccountsExampleRootApplication extends ResourceConfig {
	
	public AccountsExampleRootApplication() {
		packages(this.getClass().getPackage().getName());
	}
}
