package com.gmmapowell.swimlane.tests.models;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupTraverser;
import com.gmmapowell.swimlane.eclipse.models.BarInfo;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class BarInfoTraversalTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	GroupOfTests grp1 = context.mock(GroupOfTests.class, "grp1");
	GroupOfTests grp2 = context.mock(GroupOfTests.class, "grp2");
	GroupTraverser traverser = context.mock(GroupTraverser.class);
	List<String> tests1 = new ArrayList<>();
	List<String> tests2 = new ArrayList<>();
	@SuppressWarnings("unchecked")
	List<String> stack = context.mock(List.class, "stack");
	@SuppressWarnings("unchecked")
	List<String> expected = context.mock(List.class, "expected");
	@SuppressWarnings("unchecked")
	List<String> actual = context.mock(List.class, "actual");

	@Before
	public void setup() {
		context.checking(new Expectations() {{
			allowing(grp1).compareTo(grp1); will(returnValue(0));
			allowing(grp2).compareTo(grp1); will(returnValue(1));
			allowing(grp1).compareTo(grp2); will(returnValue(-1));
			allowing(grp2).compareTo(grp2); will(returnValue(0));
		}});
		tests1.add("case1");
		tests1.add("case2");
		tests2.add("case3");
	}

	@Test
	public void noTestsNoTraversal() {
		BarInfo bi = new BarInfo();
		bi.traverseTree(traverser);
	}

	@Test
	public void withOneGroupItIsTraversed() {
		BarInfo bi = new BarInfo();
		Sequence s = context.sequence("s");
		String tc1 = "TestClass1";
		TestCaseInfo tci = new TestCaseInfo(grp1, tc1, "case1");
		context.checking(new Expectations() {{
			oneOf(traverser).group(grp1); inSequence(s);
			oneOf(traverser).testClass(tc1); inSequence(s);
			oneOf(traverser).testCase(tci); inSequence(s);
		}});
		bi.testClass(grp1, tc1, tests1);
		bi.testCompleted(tci);
		bi.traverseTree(traverser);
	}
}
