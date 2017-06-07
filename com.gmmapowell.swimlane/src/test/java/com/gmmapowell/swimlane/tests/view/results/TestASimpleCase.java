package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import java.util.TreeSet;

import org.eclipse.swt.widgets.Tree;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultGroup;

public class TestASimpleCase extends BaseViewTest {

	@Test
	public void testThatTheTreeIsATree() throws InterruptedException {
		trv.resultsFor("bar.fred");
		specifyModel();
		Tree tree = waitForControl(shell, "hexagons.casesTree");
		assertEquals(3, tree.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		TreeSet<TestResultGroup> groups = new TreeSet<>();
		TestResultGroup g1 = context.mock(TestResultGroup.class, "g1");
		TestResultGroup g2 = context.mock(TestResultGroup.class, "g2");
		TestResultGroup g3 = context.mock(TestResultGroup.class, "g3");
		context.checking(new Expectations() {{
			allowing(g1).compareTo(g1); will(returnValue(0));
			allowing(g2).compareTo(g1); will(returnValue(1));
			allowing(g3).compareTo(g1); will(returnValue(1));
			allowing(g3).compareTo(g2); will(returnValue(1));
		}});
		groups.add(g1);
		groups.add(g2);
		groups.add(g3);
		context.checking(new Expectations() {{
			allowing(testModel).getTestResultsFor("bar.fred"); will(returnValue(groups));
		}});
		return testModel;
	}

}
