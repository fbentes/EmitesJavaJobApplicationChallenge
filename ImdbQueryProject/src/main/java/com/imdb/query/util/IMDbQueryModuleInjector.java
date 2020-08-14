/**
 * 
 */
package com.imdb.query.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author fbent
 *
 */
public class IMDbQueryModuleInjector {

	public static void initialize(Object object) {
		
		Module module = new IMDbQueryModule();
        
		Injector injector = Guice.createInjector(module);
        
		injector.injectMembers(object);
	}
}
