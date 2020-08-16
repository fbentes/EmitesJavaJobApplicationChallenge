package com.imdb.query.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author Fábio Bentes
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class WindowsPortTest {

	private WindowsPort windowsPort;
	
	@BeforeAll
	public void initialize() {
		
		windowsPort =  new WindowsPort();
	}

	@Test
	public void isPortOpenedTest() {
		
		Random random = new Random();
		
		int port = random.nextInt(Constants.PORT_TCP_MAX);

		boolean isOpened = windowsPort.isPortOpened(port);
		
		assertTrue(isOpened);
	}

	@Test
	public void isPortUsedTest() {
		
		int[] port = {135, 445, 443, 80};  // RPC

		Random random = new Random();
		int idx = random.nextInt(4);
		
		boolean isUsed = !windowsPort.isPortOpened(port[idx]);
		
		assertTrue(isUsed);
	}
	
	@Test
	public void getNextPortOpenedToUseTest() {
		
		Integer port = windowsPort.getNextPortOpenedToUse();
		
		assertTrue(port != null);
	}
}
