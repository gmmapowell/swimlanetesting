package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;

public class HexagonAccumulator implements HexagonDataModel, Accumulator, TestResultReporter {
	private final ModelDispatcher dispatcher;
	private Date buildTime;
	private Date testsCompleteTime;
	private final Map<String, Acceptance> compileAcceptances = new TreeMap<String, Acceptance>();
	private List<BarData> acceptances = new ArrayList<>();
	private final TotalOrder hexes = new TotalOrder();
	private Set<String> errors = new TreeSet<>();
	private Map<String, BarData> barsFor = new HashMap<>();
	private List<String> order;
	private final ArrayList<TestGroup> allTestClasses = new ArrayList<TestGroup>();
	
	public HexagonAccumulator(ModelDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setBuildTime(Date d) {
		this.buildTime = d;
	}
	
	@Override
	public Date getBuildTime() {
		return this.buildTime;
	}
	
	@Override
	public void testsCompleted(Date d) {
		this.testsCompleteTime = d;
	}

	@Override
	public Date getTestCompleteTime() {
		return testsCompleteTime;
	}

	@Override
	public int getHexCount() {
		return hexes.count();
	}

	public List<String> getHexes() {
		return order;
	}

	@Override
	public List<BarData> getAcceptanceTests() {
		return acceptances;
	}
	
	@Override
	public List<TestGroup> getAllTestClasses() {
		return allTestClasses;
	}

	@Override
	public void acceptance(TestGroup grp, Class<?> tc, List<Class<?>> hexes) {
		if (hexes == null || hexes.isEmpty())
			this.hexes.haveDefault();
		else
			this.hexes.addAll(hexes);
		List<String> names = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : hexes) {
			names.add(c.getName());
			if (sb.length() > 0)
				sb.append("||");
			sb.append(c.getName());
		}
		String an = sb.toString();
		Acceptance ca = compileAcceptances.get(an);
		if (ca == null) {
			ca = new Acceptance(names);
			compileAcceptances.put(an, ca);
		}
		if (!allTestClasses.contains(grp))
			allTestClasses.add(grp);
		ca.addCase(tc);
	}
	
	@Override
	public void analysisComplete() {
		this.hexes.ensureTotalOrdering(errors);
		TreeMap<String, Acceptance> tmp = new TreeMap<String, Acceptance>();
		order = this.hexes.bestOrdering(errors);
		for (Acceptance a : compileAcceptances.values()) {
			a.setMarks(order);
			// Handle an error case where because of inconsistent hex definitions, we have two
			// different acceptance tests that think they represent the same pattern (i.e. we can't distinguish two 1s in the name in different orders)
			// Merge these into a single test
			Acceptance prev = tmp.get(a.getId());
			if (prev != null) {
				prev.merge(a);
			} else {
				// This is the normal non-error case
				tmp.put(a.getId(), a);
			}
		}
		// Because we want to sort 111, 110, 101, 100, 011 ... reverse the default sorted list by adding each item on the front
		for (Acceptance a : tmp.values()) {
			acceptances.add(0, a);
			for (String c : a.classesUnderTest())
				barsFor.put(c, a);
		}
	}

	/* These errors relate to static analysis errors, such as hexagons in the wrong order.
	 * These methods are here because we override the (mocked) interface Accumulator and its tests demand that errors be reported
	 */
	@Override
	public void error(String msg) {
		// We currently don't have a mechanism to display this, so there is no point capturing or processing it
	}

	@Override
	public Set<String> getErrors() {
		return errors;
	}


	@Override
	public void tree(Tree<TestInfo> tree) {
		// It's possible that actually collecting a list of the test names would be more useful
		Map<String, AtomicInteger> classCounts = new HashMap<>();
		traverseTree(classCounts, tree);
		Set<BarData> changed = new HashSet<>();
		for (Entry<String, AtomicInteger> e : classCounts.entrySet()) {
			BarData bar = barsFor.get(e.getKey());
			if (bar == null) {
				System.out.println("There is no bar for test class " + e.getKey() +"; how did it get run?");
				continue;
			}
			((Acceptance)bar).casesForClass(e.getKey(), e.getValue().get());
			changed.add(bar);
		}
		for (BarData bd : changed)
			dispatcher.barChanged(bd);
	}

	private void traverseTree(Map<String, AtomicInteger> classCounts, Tree<TestInfo> tree) {
		TestInfo ti = tree.me();
		if (ti.type().isTestCase()) {
			AtomicInteger cnt = classCounts.get(ti.classUnderTest());
			if (cnt == null) {
				cnt = new AtomicInteger(0);
				classCounts.put(ti.classUnderTest(), cnt);
			}
			cnt.incrementAndGet();
		}
		for (Tree<TestInfo> c : tree.children())
			traverseTree(classCounts, c);
	}

	@Override
	public void testSuccess(TestInfo test) {
		String forClz = test.classUnderTest();
		BarData bar = barsFor.get(forClz);
		((Acceptance)bar).passed(forClz);
		dispatcher.barChanged(bar);
	}

	@Override
	public void testFailure(TestInfo test) {
		String forClz = test.classUnderTest();
		BarData bar = barsFor.get(forClz);
		((Acceptance)bar).failed(forClz);
		dispatcher.barChanged(bar);
	}

	@Override
	public void testRuntime(int ms) {
		// We currently don't have a mechanism to display this, so there is no point capturing or processing it
	}

	/* These errors are for individual test failures */
	@Override
	public void testError(String msg) {
		// We currently don't have a mechanism to display this, so there is no point capturing or processing it
	}
}
