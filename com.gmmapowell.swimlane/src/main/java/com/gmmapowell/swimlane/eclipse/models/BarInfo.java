package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HasABar;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public abstract class BarInfo implements BarData, HasABar {
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
	protected final Set<BarDataListener> lsnrs = new HashSet<>();

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
//		testClasses.get(key).total = cnt;
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
	public void clearGroup(GroupOfTests grp) {
		// TODO: clear this group
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}

	@Override
	public void testCompleted(TestCaseInfo ti) {
		// TODO: consolidate this
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}
}
