package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestInfoMatcher extends BaseMatcher<TestInfo> {
	private final GroupOfTests grp;
	private final String testClass;
	private final String testName;

	public TestInfoMatcher(GroupOfTests grp, String testClass, String testName) {
		this.grp = grp;
		this.testClass = testClass;
		this.testName = testName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(Object item) {
		if (!(item instanceof TestInfo))
			return false;
//		TestInfo actual = (TestInfo) item;
//		if (!expected.classUnderTest().equals(actual.classUnderTest()))
//			return false;
//		if (!expected.testName().equals(actual.testName()))
//			return false;
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
		description.appendText("]");
	}

	public static TestInfoMatcher success(GroupOfTests grp, String testClass, String testName) {
		return new TestInfoMatcher(grp, testClass, testName);
	}

}
