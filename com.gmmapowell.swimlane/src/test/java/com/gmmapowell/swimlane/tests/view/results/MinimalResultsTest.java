package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import java.util.TreeSet;

import org.eclipse.swt.widgets.Tree;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class MinimalResultsTest extends BaseViewTest {

	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		specifyModel();
		assertControls(shell, "hexagons.casesTree", "hexagons.failure");
	}

	@Test
	public void testThatTheTreeIsATree() throws InterruptedException {
		specifyModel();
		Tree tree = waitForControl(shell, "hexagons.casesTree");
		assertEquals(0, tree.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		context.checking(new Expectations() {{
			allowing(testModel).getTestResultsFor(null); will(returnValue(new TreeSet<String>()));
		}});
		return testModel;
	}

}
