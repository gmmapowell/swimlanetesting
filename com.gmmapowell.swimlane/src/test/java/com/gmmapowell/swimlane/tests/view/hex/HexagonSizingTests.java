package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.HexagonLayout;

public class HexagonSizingTests {
	@Test
	public void testACanvas590x290GivesAOf69() {
		assertEquals(69, HexagonLayout.figureA(590, 290));
	}

	@Test
	public void testACanvas290x290GivesAOf58() {
		assertEquals(58, HexagonLayout.figureA(290, 290));
	}

	@Test
	public void testACanvas290x145GivesAOf35() {
		assertEquals(34, HexagonLayout.figureA(290, 145));
	}

	@Test
	public void testACanvas295x290GivesAOf59() {
		assertEquals(59, HexagonLayout.figureA(295, 290));
	}

}
