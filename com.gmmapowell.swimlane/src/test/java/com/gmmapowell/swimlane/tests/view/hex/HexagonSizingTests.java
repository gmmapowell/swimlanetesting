package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.HexagonPaintListener;

public class HexagonSizingTests {
	HexagonPaintListener hpl = new HexagonPaintListener(null);
	
	@Test
	public void testACanvas590x290GivesAOf69() {
		assertEquals(69, hpl.figureA(590, 290));
	}

	@Test
	public void testACanvas290x290GivesAOf69() { // not sure this is true
		assertEquals(58, hpl.figureA(290, 290));
	}

	@Test
	public void testACanvas290x145GivesAOf35() {
		assertEquals(34, hpl.figureA(290, 145));
	}

}
