package com.gmmapowell.swimlane.eclipse.models;

import java.util.Set;
import java.util.TreeSet;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultClass;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultGroup;

public class AccumulatedTestResultGroup implements TestResultGroup {
	private final String groupName;
	private final Set<AccTestResultClass> classes = new TreeSet<>();

	public AccumulatedTestResultGroup(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int compareTo(TestResultGroup o) {
		return groupName.compareTo(o.name());
	}

	@Override
	public String name() {
		return groupName;
	}

	@Override
	public void add(TestInfo test) {
		String cut = test.classUnderTest();
		AccTestResultClass use = null;
		for (AccTestResultClass clz : classes) {
			if (clz.className().equals(cut)) {
				use = clz;
				break;
			}
		}
		if (use == null) {
			use = new AccTestResultClass(cut);
			classes.add(use);
		}
		use.add(test);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<TestResultClass> testClasses() {
		return (Set)classes;
	}
}
