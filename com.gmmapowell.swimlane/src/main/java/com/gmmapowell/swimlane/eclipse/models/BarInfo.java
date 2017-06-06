package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public abstract class BarInfo implements BarData {
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

	private Map<String, Tracking> testClasses = new TreeMap<>();
	protected String id;
	private Status stat = Status.OK;
	
	public BarInfo() {
	}

	@Override
	public String getId() {
		return id;
	}
	
	public abstract String getName();
	
	public void addCase(Class<?> tc) {
		testClasses.put(tc.getName(), new Tracking());
	}

	public List<String> classesUnderTest() {
		return new ArrayList<>(testClasses.keySet());
	}

	@Override
	public int[] getMarks() {
		return new int[] { 1 };
	}

	public void merge(BarInfo a) {
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
	public int getPassed() {
		int ret = 0;
		for (Entry<String, Tracking> q : testClasses.entrySet())
			ret += q.getValue().passed;
		return ret;
	}

	@Override
	public int getFailures() {
		int ret = 0;
		for (Entry<String, Tracking> q : testClasses.entrySet())
			ret += q.getValue().failed;
		return ret;
	}

	@Override
	public Status getStatus() {
		return stat;
	}
}
