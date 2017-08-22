package com.gmmapowell.swimlane.eclipse.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HasABar;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo.State;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class BarInfo implements BarData, HasABar {
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
		// TODO: clear this group
//		throw new RuntimeException("not implemented");
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}

	@Override
	public void testCompleted(TestCaseInfo ti) {
		currentState.completed++;
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}
	
	@Override
	public String toString() {
		return "BarInfo[" + getComplete() + "/" + getTotal() + "?" + isPassing() +"]";
	}
}
