package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.junit.Test;

public class MinimalResultsTest extends BaseResultsViewTest {
	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		assertControls(shell, "swimlane.casesTree", "swimlane.failure");
	}

	@Test
	public void testThatTheTreeIsATreeOfTheRightSize() throws InterruptedException {
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(0, tree.getItemCount());
		Point place = tree.getLocation();
		assertEquals(5, place.x);
		assertEquals(5, place.y);
		Point size = tree.getSize();
		assertEquals(287, size.x);
		assertEquals(280, size.y);
	}

	@Test
	public void testThatTheFailureTraceIsATableOfTheRightSize() throws InterruptedException {
		Table table = waitForControl(shell, "swimlane.failure");
		assertEquals(0, table.getItemCount());
		Point place = table.getLocation();
		assertEquals(297, place.x);
		assertEquals(5, place.y);
		Point size = table.getSize();
		assertEquals(288, size.x);
		assertEquals(280, size.y);
	}
}
