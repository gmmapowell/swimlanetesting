package com.gmmapowell.swimlane.sample.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmmapowell.swimlane.annotations.Adapter;
import com.gmmapowell.swimlane.annotations.Location;
import com.gmmapowell.swimlane.sample.code.Hex1Port1Adapter1;

@Adapter(value=Hex1Port1Adapter1.class, location=Location.SOUTHEAST)
public class AdapterTest3 {

	@Test
	public void test() {
		assertTrue(true);
	}

}
