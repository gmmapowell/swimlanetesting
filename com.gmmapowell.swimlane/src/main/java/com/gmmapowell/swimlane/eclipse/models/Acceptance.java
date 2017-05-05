package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public class Acceptance implements BarData {
	private List<String> hexs;
	private List<String> testClasses = new ArrayList<String>();
	private String id;
	private int[] marks;
	
	public Acceptance(List<String> hexs) {
		this.hexs = hexs;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void addCase(Class<?> tc) {
		testClasses.add(tc.getName());
	}

	public List<String> classesUnderTest() {
		return testClasses;
	}

	public void setMarks(List<String> order) {
		StringBuilder sb = new StringBuilder("acceptance.");
		marks = new int[order.size()];
		int i=0;
		for (String s : order) {
			int val = hexs.isEmpty() || hexs.contains(s)?1:0;
			marks[i++] = val;
			sb.append(val);
		}
		id = sb.toString();
	}

	@Override
	public int[] getMarks() {
		return marks;
	}

	public void merge(Acceptance a) {
		testClasses.addAll(a.testClasses);
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getComplete() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotal() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String toString() {
		return "Acceptance" + testClasses;
	}
}
