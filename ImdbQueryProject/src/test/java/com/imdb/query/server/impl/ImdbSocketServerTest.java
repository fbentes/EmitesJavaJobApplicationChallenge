package com.imdb.query.server.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.imdb.query.TestBase;
import com.imdb.query.client.ImdbSocketClient;
import com.imdb.query.server.ImdbSocketServer;
import com.imdb.query.util.ImdbQueryModule;

public class ImdbSocketServerTest extends TestBase {

	@Inject
	private ImdbSocketServer imdbSocketServer;
	
	@BeforeAll
	public void initializeTests() {

		Module module = new ImdbQueryModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);
	}
	
}
