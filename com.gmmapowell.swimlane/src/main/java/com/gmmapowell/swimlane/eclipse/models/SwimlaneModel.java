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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.UpdateBar;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ScreenSync;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class SwimlaneModel implements DataCentral, TestResultReporter {
	private final static Logger logger = LoggerFactory.getLogger("SwimlaneModel");
	private final ErrorAccumulator eh;
	private final ViewLayout layout;
	private final List<BarInfo> hexes = new ArrayList<>();

	private Date buildTime;
	private Date testsCompleteTime;
	private SolutionCreator currentSolution;
	public BarInfo uteData;

	// This is here for each time we create a SolutionCreator ...
	private Map<GroupOfTests, AllConstraints> constraints = new HashMap<GroupOfTests, AllConstraints>();

	private Set<DateListener> buildDateListeners = new HashSet<>();
	private Set<DateListener> testDateListeners = new HashSet<>();
	private Map<GroupOfTests, Map<String, UpdateBar>> bars = new HashMap<>();
	private ScreenSync sync;

	public SwimlaneModel(ScreenSync sync, ErrorAccumulator eh, ViewLayout layout) {
		this.sync = sync;
		this.eh = eh;
		this.layout = layout;
	}
	
	private boolean justOnce = false;
	
	@Override
	public AnalysisAccumulator startAnalysis(Date startTime) {
		logger.info("Starting analysis at " + startTime + " with justOnce = " + justOnce);
		if (currentSolution != null)
			throw new RuntimeException("I think you are running two analyses at the same time ... don't");
		if (justOnce) {
			logger.error("Can't handle re-analysis yet; see TODO list");
			return null;
		}
		justOnce = true;
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
		testDateListeners.add(lsnr);
		if (testsCompleteTime != null)
			lsnr.dateChanged(testsCompleteTime);
	}
	
	@Override
	public void testsStarted(GroupOfTests grp, Date currentDate) {
		Map<String, UpdateBar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			logger.warn("Could not find any memory of test group " + grp);
			return;
		}
		for (UpdateBar bar : lsnrs.values())
			bar.clearGroup(grp);
	}
	
	@Override
	public void testSuccess(GroupOfTests grp, String testClz, String testFn) {
		Map<String, UpdateBar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			logger.warn("Could not find any memory of test group " + grp);
			return;
		}
		UpdateBar bar = lsnrs.get(testClz);
		if (bar != null)
			bar.testCompleted(new TestCaseInfo(grp, testClz, testFn));
	}

	@Override
	public void testFailure(GroupOfTests grp, String testClz, String testFn, List<String> stack, List<String> expected, List<String> actual) {
		Map<String, UpdateBar> lsnrs = bars.get(grp);
		if (lsnrs == null) {// I think this is an error, but at runtime ...
			logger.warn("Could not find any memory of test group " + grp);
			return;
		}
		UpdateBar bar = lsnrs.get(testClz);
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
		logger.info("Running all tests");
		tr.runAll(this, this);
	}

	@Override
	public void visitGroups(GroupHandler hdlr) {
		logger.info("Visiting all groups ... " + bars.keySet());
		for (GroupOfTests g : bars.keySet())
			hdlr.runGroup(g);
		logger.info("Visited all groups");
	}

	
	@Override
	public void testsRun(Date currentDate) {
		logger.info("Tests completed at " + currentDate + " notifying " + testDateListeners);
		testsCompleteTime = currentDate;
		for (DateListener l : testDateListeners)
			l.dateChanged(testsCompleteTime);
	}


	public class SolutionHelper implements Solution {
		int chex = -1;
		PortLocation cloc;
		int apos = 0;
		UpdateBar currentBar = null;
		List<String> hexNames = new ArrayList<>();
		
		@Override
		public void beginAnalysis() {
			// TODO in update cases, need to start tracking what was already there
			// we also need to clear out the barListeners array, presumably based on which groups we are reloading ...
		}

		@Override
		public void hex(String clzName) {
			hexNames.add(clzName);
			BarInfo hi = new BarInfo();
			chex = SwimlaneModel.this.hexes.size();
			layout.addHexagon(chex, hi);
			SwimlaneModel.this.hexes.add(hi);
			currentBar = hi;
		}

		@Override
		public void testClass(GroupOfTests grp, String clzName, List<String> tests) {
			if (!bars.containsKey(grp))
				bars.put(grp, new TreeMap<>());
			Map<String, UpdateBar> map = bars.get(grp);
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
			layout.addHexagonPort(chex, loc);
			cloc = loc;
		}

		@Override
		public void adapter(String name) {
			if (chex == -1 || cloc == null)
				throw new RuntimeException("Protocol error");
			
			BarInfo adapter = new BarInfo();
			layout.addAdapter(chex, cloc, apos++, adapter);
			currentBar = adapter;
		}

		@Override
		public void acceptance(String... hexes) {
			List<String> mine = Arrays.asList(hexes);
			int[] mask = new int[this.hexNames.size()];
			for (int i=0;i<hexNames.size();i++)
				mask[i] = mine.contains(hexNames.get(i))?1:0;
			BarInfo acc = new BarInfo();
			layout.addAcceptance(mask, acc);
			currentBar = acc;			
		}
		
		@Override
		public void needsUtilityBar() {
			if (uteData == null) {
				uteData = new BarInfo();
				layout.addUtility(uteData);
			}
			currentBar = uteData;
		}

		public void analysisDone(Date completeTime) {
			SwimlaneModel.this.analysisDone(completeTime);
		}
	}
}
