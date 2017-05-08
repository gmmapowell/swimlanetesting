package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public class Acceptance implements BarData {
	public class Tracking {
		int total;
		int passed;
		int failed;

		public void merge(Tracking other) {
			total += other.total;
			passed += other.passed;
			failed += other.failed;
		}

		public int complete() {
			return passed + failed;
		}
	}

	private List<String> hexs;
	private Map<String, Tracking> testClasses = new TreeMap<>();
	private String id;
	private int[] marks;
	private Status stat = Status.OK;
	
	public Acceptance(List<String> hexs) {
		this.hexs = hexs;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void addCase(Class<?> tc) {
		testClasses.put(tc.getName(), new Tracking());
	}

	public List<String> classesUnderTest() {
		return new ArrayList<>(testClasses.keySet());
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
		for (Entry<String, Tracking> e : a.testClasses.entrySet()) {
			if (!testClasses.containsKey(e.getKey()))
				testClasses.put(e.getKey(), new Tracking());
			testClasses.get(e.getKey()).merge(e.getValue());
		}
	}

	// This is the basis of knowing how many "total" cases there should be
	// We track the classes and their respective number of tests and then add them up
	public void casesForClass(String key, int cnt) {
		testClasses.get(key).total = cnt;
	}

	public void passed(String forClz) {
		testClasses.get(forClz).passed++;
	}

	public void failed(String forClz) {
		testClasses.get(forClz).failed++;
		stat = Status.FAILURES;
	}

	@Override
	public int getTotal() {
		int ret = 0;
		for (Entry<String, Tracking> q : testClasses.entrySet())
			ret += q.getValue().total;
		return ret;
	}
	
	@Override
	public int getComplete() {
		int ret = 0;
		for (Entry<String, Tracking> q : testClasses.entrySet())
			ret += q.getValue().complete();
		return ret;
	}

	@Override
	public Status getStatus() {
		return stat ;
	}

	@Override
	public String toString() {
		return "Acceptance" + classesUnderTest();
	}
}
