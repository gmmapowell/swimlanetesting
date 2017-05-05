package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface TestInfo {
	public enum Type {
		META,
		SUITE,
		TEST;

		public boolean isTestCase() {
			return this == TEST;
		}
	}
	String classUnderTest();
	String testName();
	Type type();
	boolean hasFailed();
	List<String> stack();
	String getExpected();
	String getActual();
}
