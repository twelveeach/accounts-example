package com.example;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.example.resources.AccountsExampleRootApplication;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapUtils;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * Entry point for the stand-alone application.
 *
 */
public class Application {

	public static void main(String[] args) throws Exception {

		// Must set up Jersey-Guice first
		configureJerseyGuice();
	
		// Set up the servlets
		final ResourceConfig resourceConfig = ResourceConfig.forApplication(new AccountsExampleRootApplication());
		
		final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");

		servletContextHandler.addFilter(new FilterHolder(GuiceFilter.class), "/*", EnumSet.allOf(DispatcherType.class));
		servletContextHandler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

		//  Create the server and start it up
		final Server server = new Server(8080);
		server.setHandler(servletContextHandler);

		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}

	private static void configureJerseyGuice() {
		final ServiceLocator serviceLocator = BootstrapUtils.newServiceLocator();
		final List<ServletModule> modules = Lists.newArrayList(new ServletModule(), new AccountsExampleModule());
		BootstrapUtils.newInjector(serviceLocator, modules);
		BootstrapUtils.install(serviceLocator);
	}
}