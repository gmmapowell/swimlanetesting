package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

public class TestGroup {
	private final String cp;
	private final List<String> tcs = new ArrayList<>();

	public TestGroup(String cp) {
		this.cp = cp;
	}

	public String getClassPath() {
		return cp;
	}

	public String[] getClasses() {
		return tcs.toArray(new String[tcs.size()]);
	}

	public void addTest(String clsName) {
		tcs.add(clsName);
	}

}
