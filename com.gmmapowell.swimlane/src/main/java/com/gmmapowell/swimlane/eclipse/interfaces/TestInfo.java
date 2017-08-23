package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface TestInfo extends Comparable<TestInfo> {
	public enum State {
		SUCCESS,
		FAILURE,
		ERROR;

		public State merge(State outcome) {
			if (this == ERROR || outcome == ERROR)
				return ERROR;
			if (this == FAILURE || outcome == FAILURE)
				return FAILURE;
			return SUCCESS;
		}
	}
	GroupOfTests groupName();
	String classUnderTest();
	String testName();
	List<String> stack();
	List<String> getExpected();
	List<String> getActual();
	State outcome();
}
