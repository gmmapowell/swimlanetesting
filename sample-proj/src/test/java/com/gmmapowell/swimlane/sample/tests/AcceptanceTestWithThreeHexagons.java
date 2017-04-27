package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Acceptance;
import com.gmmapowell.swimlane.sample.code.Hexagon1;
import com.gmmapowell.swimlane.sample.code.Hexagon2;
import com.gmmapowell.swimlane.sample.code.Hexagon3;

@Acceptance({Hexagon1.class,Hexagon2.class,Hexagon3.class})
public class AcceptanceTestWithThreeHexagons {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
