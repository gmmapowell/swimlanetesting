package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Tree;
import org.junit.Test;

public class TestASimpleCase extends BaseResultsViewTest {
	@Test
	public void testThatTheViewCanCopeWithNoChosenResultsBar() throws Exception {
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(0, tree.getItemCount());
	}

	/*
	@Test
	public void testThatTheTreeHasOneProject() throws Exception {
		trv.resultsFor("bar.fred");
		specifyModel();
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(1, tree.getItemCount());
		TreeItem ti = tree.getItem(0);
		assertEquals("Project1", ti.getText());
	}

	@Test
	public void testThatTheProjectHasOneTestClass() throws Exception {
		trv.resultsFor("bar.fred");
		specifyModel();
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		TreeItem ti = tree.getItem(0);
		ti.setExpanded(true);
		assertEquals(1, ti.getItemCount());
		TreeItem ci = ti.getItem(0);
		assertEquals("SomeTests", ci.getText());
	}

	@Test
	public void testThatTheTestClassContainsATest() throws Exception {
		trv.resultsFor("bar.fred");
		specifyModel();
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		TreeItem ti = tree.getItem(0);
		ti.setExpanded(true);
		TreeItem ci = ti.getItem(0);
		ci.setExpanded(true);
		TreeItem i = ci.getItem(0);
		assertEquals(1, ci.getItemCount());
		i.setExpanded(true);
		assertEquals("testSomething", i.getText());
		assertEquals(0, i.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel();
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		TreeSet<TestResultGroup> groups = new TreeSet<>();
		TreeSet<TestResultClass> classes = new TreeSet<>();
		TreeSet<TestInfo> tests = new TreeSet<>();
		TestResultGroup g1 = context.mock(TestResultGroup.class, "g1");
		TestResultClass c1 = context.mock(TestResultClass.class, "c1");
		TestInfo t1 = context.mock(TestInfo.class, "t1");
		context.checking(new Expectations() {{
			allowing(g1).compareTo(g1); will(returnValue(0));
			allowing(c1).compareTo(c1); will(returnValue(0));
			allowing(t1).compareTo(t1); will(returnValue(0));
		}});
		groups.add(g1);
		classes.add(c1);
		tests.add(t1);
		context.checking(new Expectations() {{
			allowing(testModel).getTestResultsFor("bar.fred"); will(returnValue(groups));
			allowing(g1).name(); will(returnValue("Project1"));
			allowing(g1).testClasses(); will(returnValue(classes));
			allowing(c1).className(); will(returnValue("SomeTests"));
			allowing(c1).tests(); will(returnValue(tests));
			allowing(t1).testName(); will(returnValue("testSomething"));
		}});
		return testModel;
	}
*/
}
