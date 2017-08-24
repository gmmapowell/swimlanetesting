package com.gmmapowell.swimlane.tests.view.results;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestASimpleCase extends BaseResultsViewTest {
	@Test
	public void theViewCanCopeWithNoChosenResultsBar() throws Exception {
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(0, tree.getItemCount());
	}

	@Test
	public void theViewRequestsUpdatesFromTheBar() throws Exception {
		BarData bar = context.mock(BarData.class);
		context.checking(new Expectations() {{
			oneOf(bar).traverseTree(trv);
		}});
		trv.resultsFor(bar);
	}

	@Test
	public void traversalOfAGroupLeadsToOneTreeItem() throws Exception {
		GroupOfTests grp = context.mock(GroupOfTests.class);
		String grpName = "SomeTests";
		context.checking(new Expectations() {{
			oneOf(grp).groupName(); will(returnValue(grpName));
		}});
		trv.group(grp);
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(1, tree.getItemCount());
		TreeItem ti = tree.getItem(0);
		assertEquals(grpName, ti.getText());
	}

	@Test
	public void testThatTheTestGroupContainsATestClass() throws Exception {
		GroupOfTests grp = context.mock(GroupOfTests.class);
		String grpName = "SomeTests";
		String tc1 = "TestClass1";
		context.checking(new Expectations() {{
			oneOf(grp).groupName(); will(returnValue(grpName));
		}});
		trv.group(grp);
		trv.testClass(tc1);
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(1, tree.getItemCount());
		TreeItem ti = tree.getItem(0);
		assertEquals(grpName, ti.getText());
        ti.setExpanded(true);
        assertEquals(1, ti.getItemCount());
        TreeItem ci = ti.getItem(0);
        assertEquals(tc1, ci.getText());
        assertEquals(0, ci.getItemCount());
	}

	@Test
	public void testThatTheTestClassContainsATest() throws Exception {
		GroupOfTests grp = context.mock(GroupOfTests.class);
		String grpName = "SomeTests";
		String tc1 = "TestClass1";
		String case1 = "case1";
		TestInfo tci = context.mock(TestInfo.class);
		context.checking(new Expectations() {{
			oneOf(grp).groupName(); will(returnValue(grpName));
			oneOf(tci).testName(); will(returnValue(case1));
		}});
		trv.group(grp);
		trv.testClass(tc1);
		trv.testCase(tci);
		Tree tree = waitForControl(shell, "swimlane.casesTree");
		assertEquals(1, tree.getItemCount());
		TreeItem groupItem = tree.getItem(0);
		assertEquals(grpName, groupItem.getText());
        groupItem.setExpanded(true);
        assertEquals(1, groupItem.getItemCount());
        TreeItem classItem = groupItem.getItem(0);
        assertEquals(tc1, classItem.getText());
        assertEquals(1, classItem.getItemCount());
        TreeItem caseItem = classItem.getItem(0);
        assertEquals(case1, caseItem.getText());
        assertEquals(0, caseItem.getItemCount());
	}
}
