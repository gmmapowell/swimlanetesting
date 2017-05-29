package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Adapter;
import com.gmmapowell.swimlane.annotations.Location;
import com.gmmapowell.swimlane.sample.code.Hex1Port1;
import com.gmmapowell.swimlane.sample.code.Hex1Port1Adapter1;
import com.gmmapowell.swimlane.sample.code.Hexagon1;

@Adapter(value=Hex1Port1Adapter1.class, hexagon=Hexagon1.class, port=Hex1Port1.class, location=Location.NORTHWEST)
public class AdapterTest {

	@Test
	public void test() {
		assertTrue(true);
	}

}
