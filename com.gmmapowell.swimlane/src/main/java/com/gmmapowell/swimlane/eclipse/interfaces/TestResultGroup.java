package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Set;

public interface TestResultGroup extends Comparable<TestResultGroup> {
	String name();
	Set<TestResultClass> testClasses();
}
