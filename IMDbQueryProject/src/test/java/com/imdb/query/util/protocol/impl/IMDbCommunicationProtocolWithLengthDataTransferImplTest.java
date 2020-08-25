package com.imdb.query.util.protocol.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.google.inject.Inject;
import com.imdb.query.test.util.TestBase;
import com.imdb.query.util.Constants;
import com.imdb.query.util.IMDbQueryModuleInjector;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IMDbCommunicationProtocolWithLengthDataTransferImplTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(IMDbCommunicationProtocolWithLengthDataTransferImplTest.class);

    @Inject
    private IMDbCommunicationProtocol iMDbCommunicationProtocol;

	@BeforeAll
	public void initialize() {

		// Injetando dependências ...
		
		IMDbQueryModuleInjector.initialize(this);
		
		logger.info(Constants.STRING_EMPTY);
		logger.info("******** INICIANDO IMDbCommunicationProtocolWithLengthDataTransferImplTest ************");
		logger.info(Constants.STRING_EMPTY);
	}
	
	@Test
    @Order(1)
	public void isMatchPatternProtocolClientTest() {
		
		Random random = new Random();
		
		String[] movieValidArrayTest = 
			{"18:...E o Vento Levou",
			 "2:12",
			 "4:1917", 
			 "28:2001: Uma Odisséia no Espaço", 
			 "6:Batman",
			 "8:Um sonho"};
		
		String movieTitleExample = movieValidArrayTest[random.nextInt(movieValidArrayTest.length)];
		
		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol(movieTitleExample);
		
		assertTrue(isMatch);
		
		logger.info(getResultTest("isMatchPatternProtocolTest()", isMatch));		
	}
	
	@Test
    @Order(2)
	public void isNotMatchPatternProtocoClientTest() {
		
		Random random = new Random();
		
		String[] movieNotValidArrayTest = 
			{"...E o Vento Levou",
			 "12",
			 "1917", 
			 "2001: Uma Odisséia no Espaço", 
			 "Batman",
			 "Um sonho"};
		
		String movieTitleExample = movieNotValidArrayTest[random.nextInt(movieNotValidArrayTest.length)];
		
		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol(movieTitleExample);
		
		assertFalse(isMatch);
		
		logger.info(getResultTest("isNotMatchPatternProtocoClientTest()", !isMatch));		
	}
	
	@Test
    @Order(4)
	public void istMatchPatternProtocoServerTest() {
		
		String queryResponse = "Bamtan\nMatrix\nA Origem\n12 Anos de escravidão\n";
		
		String moviesFromServer = queryResponse.length() + ":" +queryResponse;

		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol(moviesFromServer);
		
		assertTrue(isMatch);
		
		logger.info(getResultTest("istMatchPatternProtocoServerTest()", isMatch));		
	}
	
	@Test
    @Order(5)
	public void istNotMatchPatternProtocoServerTest() {
		
		String moviesFromServer = "<html>bad request</html>";

		boolean isMatch = iMDbCommunicationProtocol.isMatchPatternProtocol(moviesFromServer);
		
		assertFalse(isMatch);
		
		logger.info(getResultTest("istNotMatchPatternProtocoServerTest()", !isMatch));		
	}
	
}
