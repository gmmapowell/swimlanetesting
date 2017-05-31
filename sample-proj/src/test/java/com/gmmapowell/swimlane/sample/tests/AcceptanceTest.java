package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Acceptance;

@Acceptance
public class AcceptanceTest {

	@Test
	public void test() throws Exception {
		Thread.sleep(2000);
		assertTrue(true);
	}

}
