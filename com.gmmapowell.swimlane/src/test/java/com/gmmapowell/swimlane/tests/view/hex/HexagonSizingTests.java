package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.HexagonPaintListener;

public class HexagonSizingTests {
	@Test
	public void testACanvas590x290GivesAOf69() {
		assertEquals(69, HexagonPaintListener.figureA(590, 290));
	}

	@Test
	public void testACanvas290x290GivesAOf58() {
		assertEquals(58, HexagonPaintListener.figureA(290, 290));
	}

	@Test
	public void testACanvas290x145GivesAOf35() {
		assertEquals(34, HexagonPaintListener.figureA(290, 145));
	}

	@Test
	public void testACanvas295x290GivesAOf59() {
		assertEquals(59, HexagonPaintListener.figureA(295, 290));
	}

}
