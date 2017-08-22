package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.SwimlaneLayout;

public class HexagonSizingTests {
	@Test
	public void testACanvas590x290GivesAOf69() {
		assertEquals(69, SwimlaneLayout.figureA(590, 290));
	}

	@Test
	public void testACanvas290x290GivesAOf54() {
		assertEquals(54, SwimlaneLayout.figureA(290, 290));
	}

	@Test
	public void testACanvas290x145GivesAOf35() {
		assertEquals(34, SwimlaneLayout.figureA(290, 145));
	}

	@Test
	public void testACanvas295x290GivesAOf55() {
		assertEquals(55, SwimlaneLayout.figureA(295, 290));
	}

}
