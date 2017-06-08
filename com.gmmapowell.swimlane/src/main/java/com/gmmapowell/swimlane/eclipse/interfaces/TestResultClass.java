package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Set;

public interface TestResultClass extends Comparable<TestResultClass> {

	String name();
	Set<TestResultTest> tests();

}
