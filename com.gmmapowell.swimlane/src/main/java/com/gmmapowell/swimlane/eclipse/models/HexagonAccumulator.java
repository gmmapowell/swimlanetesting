package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
		ArrayList<TestGroup> al = new ArrayList<TestGroup>();
		TestGroup grp = new TestGroup("");
		grp.addTest("");
		al.add(grp);
		return al;
	}

	@Override
	public void acceptance(Class<?> tc, List<Class<?>> hexes) {
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

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getErrors() {
		return errors;
	}


	@Override
	public void tree(Tree<TestInfo> tree) {
		dispatcher.barChanged(acceptances.get(0));
	}

	@Override
	public void testSuccess(TestInfo test) {
		String forClz = test.classUnderTest();
		dispatcher.barChanged(barsFor.get(forClz));
	}

	@Override
	public void testFailure(TestInfo test) {
		dispatcher.barChanged(acceptances.get(0));
	}

	@Override
	public void testRuntime(int ms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testError(String msg) {
		// TODO Auto-generated method stub
		
	}
}
