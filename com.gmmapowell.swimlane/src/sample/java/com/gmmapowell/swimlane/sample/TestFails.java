package com.gmmapowell.swimlane.sample;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFails {

	@Test
	public void fail1() {
		fail("Not yet implemented");
	}

	@Test
	public void fail2() {
		assertEquals("hello", "goodbye");
	}
}
