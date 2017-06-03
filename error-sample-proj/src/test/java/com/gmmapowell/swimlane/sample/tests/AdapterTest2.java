package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Adapter;
import com.gmmapowell.swimlane.annotations.Location;
import com.gmmapowell.swimlane.sample.code.Hex1Port1Adapter1;
import com.gmmapowell.swimlane.sample.code.Hex2Port1;

@Adapter(value=Hex1Port1Adapter1.class, port=Hex2Port1.class, location=Location.NORTHWEST)
public class AdapterTest2 {

	@Test
	public void test() {
		assertTrue(true);
	}

}
