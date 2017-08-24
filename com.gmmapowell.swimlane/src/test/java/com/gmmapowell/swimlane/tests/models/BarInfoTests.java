package com.gmmapowell.swimlane.tests.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.models.BarInfo;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class BarInfoTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	GroupOfTests grp1 = context.mock(GroupOfTests.class, "grp1");
	GroupOfTests grp2 = context.mock(GroupOfTests.class, "grp2");
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
		tests1.add("case1");
		tests1.add("case2");
		tests2.add("case3");
	}

	@Test
	public void anEmptyAcceptanceBarWouldHaveTheRightTooltip() {
		BarInfo bi = new BarInfo();
		assertEquals("Acceptance", bi.getTooltip("acceptance.111"));
	}

	@Test
	public void anAcceptanceBarWith1GroupHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		assertEquals("Acceptance - 1 group", bi.getTooltip("acceptance.111"));
	}

	@Test
	public void anAcceptanceBarWith2GroupsHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		assertEquals("Acceptance - 2 groups", bi.getTooltip("acceptance.111"));
	}

	@Test
	public void theUtilityBarWith2GroupsAndACoupleOfPassingTestsHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3"));
		assertEquals("Utility - 2 groups; 2 passed", bi.getTooltip("utility"));
	}
	
	@Test
	public void theUtilityBarWith2GroupsAndOneFailingTestHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1", stack, expected, actual));
		assertEquals("Utility - 2 groups; 1 failed", bi.getTooltip("utility"));
	}
	
	@Test
	public void theUtilityBarWith2GroupsOnePassingAndOneFailingTestHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack, expected, actual));
		assertEquals("Utility - 2 groups; 1 passed, 1 failed", bi.getTooltip("utility"));
	}
	
	@Test
	public void aBusinessLogicBarWith1GroupAndOneErrorHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1", stack));
		assertEquals("Business - 1 group; 1 error", bi.getTooltip("business.0"));
	}
	
	@Test
	public void aBusinessLogicBarWith1GroupAndTwoErrorsHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1", stack));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		assertEquals("Business - 1 group; 2 errors", bi.getTooltip("business.0"));
	}
	
	@Test
	public void aBusinessLogicBarWith1GroupAndOneFailedAnd1ErrorHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1", stack, expected, actual));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		assertEquals("Business - 1 group; 1 failed, 1 error", bi.getTooltip("business.0"));
	}
	
	@Test
	public void anAdapterBarWith2GroupsAndOneSuccessAndTwoErrorsHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack));
		assertEquals("Adapter - 2 groups; 1 passed, 2 errors", bi.getTooltip("adapter.0.nw.0"));
	}
	
	@Test
	public void anAdapterBarWith2GroupsAndOneOfEachHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack, expected, actual));
		assertEquals("Adapter - 2 groups; 1 passed, 1 failed, 1 error", bi.getTooltip("adapter.0.nw.0"));
	}

	@Test
	public void theTooltipIsTidiedUpWhenWeResetTheTooltipOnClearGroupOfOneError() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack, expected, actual));
		assertEquals("Utility - 2 groups; 1 passed, 1 failed, 1 error", bi.getTooltip("utility"));
		bi.clearGroup(grp2); // reset the failure
		assertEquals("Utility - 2 groups; 1 passed, 1 error", bi.getTooltip("utility"));
	}
	
	@Test
	public void theTooltipIsTidiedUpWhenWeResetTheTooltipOnClearGroupOfASuccessAndFail() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack, expected, actual));
		assertEquals("Utility - 2 groups; 1 passed, 1 failed, 1 error", bi.getTooltip("utility"));
		bi.clearGroup(grp1); // reset the success & error
		assertEquals("Utility - 2 groups; 1 failed", bi.getTooltip("utility"));
	}
	
	@Test
	public void theTooltipIsClearedWhenWeResetTheTooltipOnClearGroupOfASuccessAndFail() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		bi.testClass(grp2, "TestClass2", tests2);
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case1"));
		bi.testCompleted(new TestCaseInfo(grp1, "TestClass1", "case2", stack));
		bi.testCompleted(new TestCaseInfo(grp2, "TestClass2", "case3", stack, expected, actual));
		assertEquals("Utility - 2 groups; 1 passed, 1 failed, 1 error", bi.getTooltip("utility"));
		bi.clearGroup(grp1); // reset the success & error
		bi.clearGroup(grp2); // reset the failure
		assertEquals("Utility - 2 groups", bi.getTooltip("utility"));
	}
}
