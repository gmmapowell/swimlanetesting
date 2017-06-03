package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Acceptance;
import com.gmmapowell.swimlane.sample.code.Hexagon1;
import com.gmmapowell.swimlane.sample.code.Hexagon3;

@Acceptance({Hexagon1.class,Hexagon3.class})
public class AcceptanceTest1 {

	@Test
	public void test() throws Exception {
		Thread.sleep(2000);
		assertTrue(true);
	}

}
