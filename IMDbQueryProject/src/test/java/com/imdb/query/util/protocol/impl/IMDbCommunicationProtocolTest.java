package com.imdb.query.util.protocol.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.imdb.query.test.util.TestBase;
import com.imdb.query.util.Constants;
import com.imdb.query.util.protocol.IMDbCommunicationProtocol;

/**
 * @author Fábio Bentes
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IMDbCommunicationProtocolTest extends TestBase {

    private static final Logger logger = LogManager.getLogger(IMDbCommunicationProtocolTest.class);

    private IMDbCommunicationProtocol iMDbCommunicationProtocol;

	@BeforeAll
	public void initialize() {

		iMDbCommunicationProtocol = new IMDbCommunicationProtocolImpl();	
		
		logger.info(Constants.STRING_EMPTY);
		logger.info("******** INICIANDO IMDbCommunicationProtocolTest ************");
		logger.info(Constants.STRING_EMPTY);
	}
	
	@Test
    @Order(1)
	public void isMatchPatternProtocolClientTest() {
		
		Random random = new Random();
		
		String[] movieValidArrayTest = 
			{Constants.PREFIX_PROTOCOL + "...E o Vento Levou" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "12" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "1917, 2001: Uma Odisséia no Espaço" + Constants.SUFIX_PROTOCOL, 
			 Constants.PREFIX_PROTOCOL + "Batman" + Constants.SUFIX_PROTOCOL,
			 Constants.PREFIX_PROTOCOL + "Um sonho" + Constants.SUFIX_PROTOCOL};
		
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
			 "1917, 2001: Uma Odisséia no Espaço", 
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
		
		String moviesFromServer = 
				Constants.PREFIX_PROTOCOL + 
				"Bamtan\nMatrix\nA Origem\n12 Anos de escravidão\n" + 
				Constants.SUFIX_PROTOCOL;

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
	
	@AfterAll
	public void end() {
		logger.info(Constants.STRING_EMPTY);
		logger.info("******** FINALIZADO IMDbCommunicationProtocolTest ************");
		logger.info(Constants.STRING_EMPTY);
	}
}
