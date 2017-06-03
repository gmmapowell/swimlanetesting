package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Acceptance;
import com.gmmapowell.swimlane.sample.code.Hexagon1;
import com.gmmapowell.swimlane.sample.code.Hexagon2;
import com.gmmapowell.swimlane.sample.code.Hexagon3;

@Acceptance({Hexagon1.class,Hexagon2.class})
public class AcceptanceTest2 {

	@Test
	public void test() throws InterruptedException {
		Thread.sleep(1000);
		assertEquals(42, 6*7);
	}

}
