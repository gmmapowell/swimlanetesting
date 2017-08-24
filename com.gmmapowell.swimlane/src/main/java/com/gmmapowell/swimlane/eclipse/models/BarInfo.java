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
import com.gmmapowell.swimlane.eclipse.interfaces.GroupTraverser;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo.State;
import com.gmmapowell.swimlane.eclipse.interfaces.UpdateBar;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class BarInfo implements BarData, UpdateBar {
	static class ConsolidatedState {
		State state = State.SUCCESS;
		int total;
		int completed;
		int passed, failed, errors;
	}
	protected ConsolidatedState currentState = new ConsolidatedState();
	protected final Map<GroupOfTests, ConsolidatedState> groups = new HashMap<>();
	protected final Set<BarDataListener> lsnrs = new HashSet<>();

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
	public String getTooltip(String barid) {
		StringBuilder sb = new StringBuilder();
		String tn = typeName(barid);
		if (tn != null) {
			sb.append(tn);
		}
		if (!groups.isEmpty()) {
			sb.append(" - ");
			sb.append(groups.size());
			sb.append(" group");
			if (groups.size() > 1)
				sb.append("s");
			String sep = "; ";
			if (currentState.passed > 0) {
				sb.append(sep);
				sb.append(currentState.passed);
				sb.append(" passed");
				sep = ", ";
			}
			if (currentState.failed > 0) {
				sb.append(sep);
				sb.append(currentState.failed);
				sb.append(" failed");
				sep = ", ";
			}
			if (currentState.errors > 0) {
				sb.append(sep);
				sb.append(currentState.errors);
				sb.append(" error");
				if (currentState.errors != 1)
					sb.append("s");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean isPassing() {
		return currentState.state == State.SUCCESS;
	}

	@Override
	public void clearGroup(GroupOfTests grp) {
		ConsolidatedState cs = groups.get(grp);
		if (cs != null) {
			currentState.completed -= cs.completed; // this has to be before we reset the count to 0
			currentState.passed -= cs.passed;
			currentState.failed -= cs.failed;
			currentState.errors -= cs.errors;
			cs.completed = cs.passed = cs.failed = cs.errors = 0;
			cs.state = State.SUCCESS; // and this has to be before we reset the state, otherwise it will include any errors from last time
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
		switch (ti.outcome()) {
		case SUCCESS:
			cs.passed++;
			currentState.passed++;
			break;
		case FAILURE:
			cs.failed++;
			currentState.failed++;
			break;
		case ERROR:
			cs.errors++;
			currentState.errors++;
			break;
		}
		currentState.completed++;
		currentState.state = currentState.state.merge(ti.outcome());
		for (BarDataListener lsnr : lsnrs)
			lsnr.barChanged(this);
	}
	
	@Override
	public void traverseTree(GroupTraverser traverser) {
		// TODO Auto-generated method stub
		
	}

	private State consolidate(Collection<ConsolidatedState> values) {
		State ret = State.SUCCESS;
		for (ConsolidatedState cs : values)
			ret = ret.merge(cs.state);
		return ret;
	}

	private String typeName(String barid) {
		if (barid.startsWith("business."))
			return "Business";
		else if (barid.startsWith("adapter."))
			return "Adapter";
		else if (barid.startsWith("acceptance."))
			return "Acceptance";
		else if (barid.equals("utility"))
			return "Utility";
		else 
			return barid;
	}

	@Override
	public String toString() {
		return "BarInfo[" + getComplete() + "/" + getTotal() + "?" + isPassing() +"]";
	}
}
