package com.imdb.query.test.util;
public class TestBase {

	protected String getResultTest(String methodTest, boolean isPassed) {
		
		return String.format("%s %s",methodTest, (isPassed ? " passou." : "n�o passou."));
	}
}
