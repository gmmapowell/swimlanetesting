package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.models.GroupOfTests;

public interface TestInfo extends Comparable<TestInfo> {
	public enum Type {
		META,
		SUITE,
		TEST;

		public boolean isTestCase() {
			return this == TEST;
		}
	}
	GroupOfTests groupName();
	String classUnderTest();
	String testName();
	Type type();
	boolean hasFailed();
	List<String> stack();
	String getExpected();
	String getActual();
}
