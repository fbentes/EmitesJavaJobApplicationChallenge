package com.imdb.query.util.network;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.imdb.query.test.TestBase;

@TestInstance(Lifecycle.PER_CLASS)
public class AddressNetworkValidatorTest extends TestBase {

    private static final Logger logger = LogManager.getLogger("AddressNetworkValidatorTest");

    private AddressNetworkValidator addressNetworkValidator;
	
	@BeforeAll
	public void initialize() {
		
		addressNetworkValidator =  new AddressNetworkValidator();
	}
	
	@Test
	public void isIpValidTest() {
		
		String ip = "192.168.0.14";
		
		boolean isIpValid = addressNetworkValidator.isIpValid(ip);
		
		assertTrue(isIpValid);
		
		logger.info(getResultTest("NullPortIsInValidTest()", isIpValid));
	}
	
	@Test
	public void isIpInvalidTest() {
		
		String ip = "678.168.0.14";
		
		boolean isIpValid = addressNetworkValidator.isIpValid(ip);
		
		assertFalse(isIpValid);
		
		logger.info(getResultTest("isIpInvalidTest()", !isIpValid));
	}
}
