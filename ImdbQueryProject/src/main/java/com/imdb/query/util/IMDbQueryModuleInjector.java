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
 * @since 09/08/2020
 *
 */
public class IMDbQueryModuleInjector {

	public static void initialize(Object object) {
		
		Module module = new IMDbQueryModule();
        
		Injector injector = Guice.createInjector(module);
        
		injector.injectMembers(object);
	}
}
