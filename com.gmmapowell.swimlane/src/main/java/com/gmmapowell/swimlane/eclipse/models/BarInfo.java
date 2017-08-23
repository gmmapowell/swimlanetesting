package com.gmmapowell.swimlane.eclipse.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.UpdateBar;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo.State;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class BarInfo implements BarData, UpdateBar {
	static class ConsolidatedState {
		State state = State.SUCCESS;
		int total;
		int completed;
	}
	protected ConsolidatedState currentState = new ConsolidatedState();
	protected final Map<GroupOfTests, ConsolidatedState> groups = new HashMap<>();
	protected final Set<BarDataListener> lsnrs = new HashSet<>();

	public BarInfo() {
	}

	@Override
	public void addTestListener(BarDataListener lsnr) {
		lsnrs.add(lsnr);
	}

	// I think we need something to reject old data during updates
	@Override
	public void testClass(GroupOfTests grp, String clzName, List<String> tests) {
		if (!groups.containsKey(grp))
			groups.put(grp, new ConsolidatedState());
		ConsolidatedState cs = groups.get(grp);
		cs.total += tests.size();
		currentState.total += tests.size();
	}

	@Override
	public int getTotal() {
		return currentState.total;
	}
	
	@Override
	public int getComplete() {
		return currentState.completed;
	}

	@Override
	public boolean isPassing() {
		return currentState.state == State.SUCCESS;
	}

	@Override
	public void clearGroup(GroupOfTests grp) {
		ConsolidatedState cs = groups.get(grp);
		if (cs != null) {
			currentState.completed -= cs.completed;
			currentState.state = consolidate(groups.values());
		}
		
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}

	@Override
	public void testCompleted(TestCaseInfo ti) {
		GroupOfTests grp = ti.groupName();
		if (!groups.containsKey(grp))
			groups.put(grp, new ConsolidatedState());
		ConsolidatedState cs = groups.get(grp);
		cs.completed++;
		cs.state = cs.state.merge(ti.outcome());
		currentState.completed++;
		currentState.state = currentState.state.merge(ti.outcome());
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}
	
	private State consolidate(Collection<ConsolidatedState> values) {
		State ret = State.SUCCESS;
		for (ConsolidatedState cs : values)
			ret = ret.merge(cs.state);
		return ret;
	}

	@Override
	public String toString() {
		return "BarInfo[" + getComplete() + "/" + getTotal() + "?" + isPassing() +"]";
	}
}
