package com.imdb.query.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsável para suprimir mensagens de alertas no start.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 21/08/2020
 *
 */
public class StartBase {

	private static final Logger logger = LogManager.getLogger(StartClient.class);

	protected static String INPUT_QUIT = "quit";
	
	private static String javaVersion;
	
	private static boolean isJavaVersion8OrHigher() {
		
		// Versão mínima aceitável: 1.8.0_265
		// Versão  "14.0.1" não é compatível
		
		javaVersion = System.getProperty("java.version");   
		
		logger.info("javaVersion = " + javaVersion);
		
		String majorVersionOnly = javaVersion.substring(0, 3);
		
		logger.info("majorVersionOnly = " + majorVersionOnly);
		
		Double versionValue = Double.parseDouble(majorVersionOnly);
		
		logger.info("versionValue = " + versionValue);
		
		return versionValue >= 1.8;
	}
	
	/**
	 * Método para desabilitar warnings no console devido a diferentes versões do Java, pois poluem a tela.
	 * 
	 * OBS.: Carece de um melhor tratamento no futuro !
	 * 
	 */
	protected static void disableAccessWarnings() {
		
		if(!isJavaVersion8OrHigher()) {
			
			logger.info("");
			
			logger.info("A versão atual do seu Java é " + javaVersion +".");
			logger.info("Esse projeto só permite versões do Java >= 1.8 !");

			logger.info("");
			
			Runtime.getRuntime().exit(0);
		}
		
        try {
            
        	Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            
        	Field field = unsafeClass.getDeclaredField("theUnsafe");
            
        	field.setAccessible(true);
            
        	Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            
            Field loggerField = loggerClass.getDeclaredField("logger");
            
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
            
        } catch (Exception ignored) {
        	
        	// Exceção ignorada devido ao propósito do método.
        }
    }
}
