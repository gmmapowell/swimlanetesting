package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import java.util.TreeSet;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.jmock.Expectations;
import org.junit.Test;

public class MinimalResultsTest extends BaseViewTest {
/*
	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		specifyModel();
		assertControls(shell, "hexagons.casesTree", "hexagons.failure");
	}

	@Test
	public void testThatTheTreeIsATreeOfTheRightSize() throws InterruptedException {
		specifyModel();
		Tree tree = waitForControl(shell, "hexagons.casesTree");
		assertEquals(0, tree.getItemCount());
		Point size = tree.getSize();
		assertEquals(287, size.x);
		assertEquals(280, size.y);
	}

	@Test
	public void testThatTheFailureTraceIsATableOfTheRightSize() throws InterruptedException {
		specifyModel();
		Table table = waitForControl(shell, "hexagons.failure");
		assertEquals(0, table.getItemCount());
		Point size = table.getSize();
		assertEquals(288, size.x);
		assertEquals(280, size.y);
	}

	protected void specifyModel() throws InterruptedException {
		pushModel();
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		context.checking(new Expectations() {{
			allowing(testModel).getTestResultsFor(null); will(returnValue(new TreeSet<String>()));
		}});
		return testModel;
	}
*/
}
