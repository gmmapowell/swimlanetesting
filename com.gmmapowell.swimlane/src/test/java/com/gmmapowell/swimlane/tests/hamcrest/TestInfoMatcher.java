package com.gmmapowell.swimlane.tests.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestInfoMatcher extends BaseMatcher<TestInfo> {
	private TestInfo expected;
	private boolean crs;

	private TestInfoMatcher(TestInfo ti, boolean crs) {
		this.expected = ti;
		this.crs = crs;
	}

	@Override
	public boolean matches(Object item) {
		if (!(item instanceof TestInfo))
			return false;
		TestInfo actual = (TestInfo) item;
		if (!expected.classUnderTest().equals(actual.classUnderTest()))
			return false;
		if (!expected.testName().equals(actual.testName()))
			return false;
		if (crs) { // check run-time status
			if (expected.hasFailed() != actual.hasFailed())
				return false;
			if (expected.stack() == null && actual.stack() == null)
				; // that's OK
			else if (expected.stack() == null || !expected.stack().equals(actual.stack()))
				return false;
			if (expected.getExpected() == null && actual.getExpected() == null)
				;
			else if (expected.getExpected() == null || !expected.getExpected().equals(actual.getExpected()))
				return false;
			if (expected.getActual() == null && actual.getActual() == null)
				;
			else if (expected.getActual() == null || !expected.getActual().equals(actual.getActual()))
				return false;
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(this.expected);
	}

	public static TestInfoMatcher of(TestInfo ti) {
		return new TestInfoMatcher(ti, true);
	}

	public static TestInfoMatcher of(TestInfo me, boolean checkRunStatus) {
		return new TestInfoMatcher(me, checkRunStatus);
	}

}
