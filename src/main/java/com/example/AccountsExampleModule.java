package com.example;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

import com.example.resources.AccountResource;
import com.example.storage.DataStore;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * Guice bindings for the application.
 */
public class AccountsExampleModule extends ServletModule {
	 
    @Override
    protected void configureServlets() {
        bind(DefaultServlet.class).in(Singleton.class);
        bind(AccountResource.class).in(Singleton.class);
        bind(DataStore.class).in(Singleton.class);
 
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
    }
}