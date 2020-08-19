/**
 * 
 */
package com.imdb.query.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Inicializa as dependências necessárias.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 13/08/2020
 *
 */
public class IMDbQueryModuleInjector {

	private static Module module;
	
	private static Injector injector;
	
	static {
		module = new IMDbQueryModule();
		
		injector = Guice.createInjector(module);
	}
	
	public static Injector initialize(Object object) {
		
		injector.injectMembers(object);
		
		return injector;
	}
}
