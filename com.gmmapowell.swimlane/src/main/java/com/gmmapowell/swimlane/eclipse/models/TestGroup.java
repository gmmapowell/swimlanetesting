package com.gmmapowell.swimlane.eclipse.models;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;

public class TestGroup implements GroupOfTests {
	private final List<File> cp;
	private final Set<String> tcs = new TreeSet<>();
	private final String name;

	public TestGroup(String name, List<File> cp) {
		this.name = name;
		this.cp = cp;
	}

	public String groupName() {
		return name;
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
