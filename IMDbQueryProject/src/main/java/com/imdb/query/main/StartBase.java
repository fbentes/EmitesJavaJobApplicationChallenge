package com.imdb.query.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Respons�vel para suprimir mensagens de alertas no start.
 * 
 * @author F�bio Bentes
 * @version 1.0.0.0
 * @since 21/08/2020
 *
 */
public class StartBase {

	/**
	 * M�todo para desabilitar warnings devido a diferentes vers�es do Java.
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
        }
    }
}
