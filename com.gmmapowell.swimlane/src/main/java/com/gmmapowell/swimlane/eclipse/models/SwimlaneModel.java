package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HasABar;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ScreenSync;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class SwimlaneModel implements DataCentral, TestResultReporter {
	private final ErrorAccumulator eh;
	private final ViewLayout layout;
	private final List<HexInfo> hexes = new ArrayList<>();

	private Date buildTime;
	private Date testsCompleteTime;
	private SolutionCreator currentSolution;
	public UtilityInfo uteData;

	// This is here for each time we create a SolutionCreator ...
	private Map<GroupOfTests, AllConstraints> constraints = new HashMap<GroupOfTests, AllConstraints>();

	private Map<GroupOfTests, Object> groups = new TreeMap<>();
	private Set<DateListener> buildDateListeners = new HashSet<>();
	private Set<DateListener> testDateListeners = new HashSet<>();
	private Map<GroupOfTests, Map<String, HasABar>> bars = new HashMap<>();
	private ScreenSync sync;

	public SwimlaneModel(ScreenSync sync, ErrorAccumulator eh, ViewLayout layout) {
		this.sync = sync;
		this.eh = eh;
		this.layout = layout;
		
	}
	@Override
	public AnalysisAccumulator startAnalysis(Date startTime) {
		if (currentSolution != null)
			throw new RuntimeException("I think you are running two analyses at the same time ... don't");
		return new SolutionCreator(sync, eh, new SolutionHelper(), constraints);
	}
	
	private void analysisDone(Date completeTime) {
		this.buildTime = completeTime;
		for (DateListener l : buildDateListeners)
			l.dateChanged(completeTime);
	}

	@Override
	public void addBuildDateListener(DateListener lsnr) {
		buildDateListeners.add(lsnr);
		if (buildTime != null)
			lsnr.dateChanged(buildTime);
	}
	
	@Override
	public void addTestDateListener(DateListener lsnr) {
		System.out.println("Adding test date listener " + lsnr);
		testDateListeners.add(lsnr);
		if (testsCompleteTime != null)
			lsnr.dateChanged(testsCompleteTime);
	}
	
	@Override
	public void testsStarted(GroupOfTests grp, Date currentDate) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void testCount(GroupOfTests grp, int cnt) {
		Map<String, HasABar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			System.out.println("Could not find any memory of test group " + grp);
			return;
		}
		for (HasABar bar : lsnrs.values())
			bar.clearGroup(grp);
	}
	
	@Override
	public void testSuccess(GroupOfTests grp, String testClz, String testFn) {
		Map<String, HasABar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			System.out.println("Could not find any memory of test group " + grp);
			return;
		}
		HasABar bar = lsnrs.get(testClz);
		if (bar != null)
			bar.testCompleted(new TestCaseInfo(grp, testClz, testFn));
	}

	@Override
	public void testFailure(GroupOfTests grp, String testClz, String testFn, List<String> stack, List<String> expected, List<String> actual) {
		Map<String, HasABar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			System.out.println("Could not find any memory of test group " + grp);
			return;
		}
		HasABar bar = lsnrs.get(testClz);
		if (bar != null)
			bar.testCompleted(new TestCaseInfo(grp, testClz, testFn, stack, expected, actual));
	}

	@Override
	public void testError(GroupOfTests grp, String testClz, String testFn, List<String> stack) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void testsCompleted(GroupOfTests grp, Date currentDate) {
	}

	public void runAllTests(TestRunner tr) {
		System.out.println("Running all tests");
		tr.runAll(this, this);
	}

	@Override
	public void visitGroups(GroupHandler hdlr) {
		System.out.println("Visiting all groups");
		for (GroupOfTests g : groups.keySet())
			hdlr.runGroup(g);
		System.out.println("Visited all groups");
	}

	
	@Override
	public void testsRun(Date currentDate) {
		System.out.println("Tests completed at " + currentDate + " notifying " + testDateListeners);
		testsCompleteTime = currentDate;
		for (DateListener l : testDateListeners)
			l.dateChanged(testsCompleteTime);
	}


	public class SolutionHelper implements Solution {
		int chex = -1;
		PortLocation cloc;
		int apos = 0;
		HasABar currentBar = null;
		
		@Override
		public void beginAnalysis() {
			// TODO in update cases, need to start tracking what was already there
			// we also need to clear out the barListeners array, presumably based on which groups we are reloading ...
		}

		@Override
		public void hex(String clzName) {
			HexInfo hi = new HexInfo(clzName);
			chex = SwimlaneModel.this.hexes.size();
			layout.addHexagon(chex, hi);
			SwimlaneModel.this.hexes.add(hi);
			currentBar = hi;
		}

		@Override
		public void testClass(GroupOfTests grp, String clzName, List<String> tests) {
			if (!bars.containsKey(grp))
				bars.put(grp, new TreeMap<>());
			Map<String, HasABar> map = bars.get(grp);
			// may need to clear it out?
			if (currentBar != null) {
				map.put(clzName, currentBar);
				currentBar.testClass(grp, clzName, tests);
			}
		}

		@Override
		public void port(PortLocation loc, String port) {
			if (chex == -1)
				throw new RuntimeException("Protocol error");
			HexInfo hi = hexes.get(chex);
			PortInfo pi = new PortInfo(port, loc);
			hi.addPort(pi);
			layout.addHexagonPort(chex, loc, pi);
			cloc = loc;
		}

		@Override
		public void adapter(String name) {
			if (chex == -1 || cloc == null)
				throw new RuntimeException("Protocol error");
			
			AdapterInfo adapter = new AdapterInfo(name);
			layout.addAdapter(chex, cloc, apos++, adapter);
			currentBar = adapter;
		}

		@Override
		public void acceptance(String... hexes) {
			Acceptance acc = new Acceptance(Arrays.asList(hexes));
			layout.addAcceptance(new int[] { 1 }, acc);
			currentBar = acc;			
		}
		
		@Override
		public void needsUtilityBar() {
			if (uteData == null) {
				uteData = new UtilityInfo();
				layout.addUtility(uteData);
			}
			currentBar = uteData;
		}

		public void analysisDone(Date completeTime) {
			SwimlaneModel.this.analysisDone(completeTime);
		}
	}
}
