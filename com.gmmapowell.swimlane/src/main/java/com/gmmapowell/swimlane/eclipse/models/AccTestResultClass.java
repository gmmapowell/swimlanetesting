package com.gmmapowell.swimlane.eclipse.models;

import java.util.HashSet;
import java.util.Set;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultClass;

public class AccTestResultClass implements TestResultClass {
	private final String clzName;
	private final Set<TestInfo> tests = new HashSet<>();

	public AccTestResultClass(String cut) {
		this.clzName = cut;
	}

	@Override
	public int compareTo(TestResultClass o) {
		return clzName.compareTo(o.className());
	}

	@Override
	public String className() {
		return clzName;
	}
	
	public void add(TestInfo test) {
		tests.add(test);
	}

	@Override
	public Set<TestInfo> tests() {
		return tests;
	}

}
