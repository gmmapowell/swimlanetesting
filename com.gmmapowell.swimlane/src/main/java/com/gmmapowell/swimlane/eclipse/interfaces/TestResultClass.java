package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Set;

public interface TestResultClass extends Comparable<TestResultClass> {
	String className();
	Set<TestInfo> tests();
}
