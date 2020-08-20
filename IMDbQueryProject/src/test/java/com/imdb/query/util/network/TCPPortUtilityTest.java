package com.imdb.query.util.network;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.imdb.query.test.TestBase;
import com.imdb.query.util.Constants;

/**
 * @author Fábio Bentes
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TCPPortUtilityTest extends TestBase {

    private static final Logger logger = LogManager.getLogger("TCPPortUtilityTest");

    private TCPPortUtility tcpPortValidator;
	
	@BeforeAll
	public void initialize() {
		
		tcpPortValidator =  new TCPPortUtility();
	}

	@Test
	public void NullPortIsInValidTest() {
			
		boolean isTcpPortValid = tcpPortValidator.isPortValid(null);
		
		assertFalse(isTcpPortValid);
		
		logger.info(getResultTest("NullPortIsInValidTest()", !isTcpPortValid));
	}

	@Test
	public void isPortValidTest() {
		
		Random random = new Random();
		
		int port = random.nextInt(Constants.PORT_TCP_MAX);

		boolean isPortValid = tcpPortValidator.isPortValid(port);
		
		assertTrue(isPortValid);

		logger.info(getResultTest("isPortValidTest()", isPortValid));
	}

	@Test
	public void isPortNotValidTest() {
		
		int[] port = {89345, 89563, 92745, 129749}; 

		Random random = new Random();
		int idx = random.nextInt(4);
		
		boolean isPortValid = tcpPortValidator.isPortValid(port[idx]);
		
		assertFalse(isPortValid);

		logger.info(getResultTest("isPortNotValidTest()", !isPortValid));
	}

	@Test
	public void isPortOpenedTest() {
		
		Random random = new Random();
		
		int port = random.nextInt(Constants.PORT_TCP_MAX);

		boolean isOpened = tcpPortValidator.isPortOpened(port);
		
		assertTrue(isOpened);

		logger.info(getResultTest("isPortOpenedTest()", isOpened));
	}

	@Test
	public void isPortUsedTest() {
		
		int[] port = {445, 443, 80, 135};  //  445, HTTPS é 443, HTTP é 80 e RPC é 135.

		Random random = new Random();
		int idx = random.nextInt(4);
		
		boolean isUsed = !tcpPortValidator.isPortOpened(port[idx]);
		
		assertTrue(isUsed);

		logger.info(getResultTest("isPortUsedTest()", isUsed));
	}
	
	@Test
	public void getNextPortOpenedToUseTest() {
		
		Integer port = tcpPortValidator.getNextPortOpenedToUse();
		
		assertTrue(port != null);

		logger.info(getResultTest("getNextPortOpenedToUseTest()", port != null));
	}
}
