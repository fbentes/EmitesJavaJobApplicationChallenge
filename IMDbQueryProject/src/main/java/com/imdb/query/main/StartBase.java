package com.imdb.query.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Responsável para suprimir mensagens de alertas no start.
 * 
 * @author Fábio Bentes
 * @version 1.0.0.0
 * @since 21/08/2020
 *
 */
public class StartBase {

	protected static String INPUT_QUIT = "quit";
	
	/**
	 * Método para desabilitar warnings no console devido a diferentes versões do Java, pois poluem a tela.
	 * 
	 * OBS.: Carece de um melhor tratamento no futuro !
	 * 
	 */
	public static void disableAccessWarnings() {
		
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
