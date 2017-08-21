package com.gmmapowell.swimlane.testsupport.matchers;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo.State;

public class TestInfoMatcher extends BaseMatcher<TestInfo> {
	private final State outcome;
	private final GroupOfTests grp;
	private final String testClass;
	private final String testName;

	public TestInfoMatcher(State outcome, GroupOfTests grp, String testClass, String testName) {
		this.outcome = outcome;
		this.grp = grp;
		this.testClass = testClass;
		this.testName = testName;
	}

	@Override
	public boolean matches(Object item) {
		if (!(item instanceof TestInfo))
			return false;
		TestInfo actual = (TestInfo) item;
		if (outcome != actual.outcome())
			return false;
		if (!testClass.equals(actual.classUnderTest()))
			return false;
		if (!testName.equals(actual.testName()))
			return false;
//		if (crs) { // check run-time status
//			if (expected.hasFailed() != actual.hasFailed())
//				return false;
//			if (expected.stack() == null && actual.stack() == null)
//				; // that's OK
//			else if (expected.stack() == null || !expected.stack().equals(actual.stack()))
//				return false;
//			if (expected.getExpected() == null && actual.getExpected() == null)
//				;
//			else if (expected.getExpected() == null || !expected.getExpected().equals(actual.getExpected()))
//				return false;
//			if (expected.getActual() == null && actual.getActual() == null)
//				;
//			else if (expected.getActual() == null || !expected.getActual().equals(actual.getActual()))
//				return false;
//		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("TestInfo[");
		description.appendValue(this.grp);
		description.appendText(",");
		description.appendValue(this.testClass);
		description.appendText(",");
		description.appendValue(this.testName);
		description.appendText(":");
		description.appendValue(this.outcome);
		description.appendText("]");
	}

	public static TestInfoMatcher success(GroupOfTests grp, String testClass, String testName) {
		return new TestInfoMatcher(State.SUCCESS, grp, testClass, testName);
	}

	public static TestInfoMatcher failure(GroupOfTests grp, String testClass, String testName, List<String> stack, List<String> expected, List<String> actual) {
		return new TestInfoMatcher(State.FAILURE, grp, testClass, testName);
	}

}
