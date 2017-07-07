package com.gmmapowell.swimlane.eclipse.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestGroup {
	private final List<File> cp;
	private final List<String> tcs = new ArrayList<>();

	public TestGroup(String name, List<File> cp) {
		this.cp = cp;
	}

	public String getClassPath() {
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (File s : cp) {
			sb.append(sep);
			sb.append(s.getPath());
			sep = ":";
		}
		return sb.toString();
	}

	public String[] getClasses() {
		return tcs.toArray(new String[tcs.size()]);
	}

	public void addTest(String clsName) {
		tcs.add(clsName);
	}

}
