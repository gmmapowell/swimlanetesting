package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultGroup;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;

public class SwimlaneModel implements DataCentral, TestResultReporter {
	private final ErrorAccumulator eh;
	private final ViewLayout layout;
	private final List<HexInfo> hexes = new ArrayList<>();

	private Date buildTime;
	private Date testsCompleteTime;
	private SolutionCreator currentSolution;

	// This is here for each time we create a SolutionCreator ...
	private Map<GroupOfTests, AllConstraints> constraints = new HashMap<GroupOfTests, AllConstraints>();

	private final Map<String, Acceptance> compileAcceptances = new TreeMap<String, Acceptance>();
	private List<BarData> acceptances = new ArrayList<>();
	private final Map<String, HexInfo> hexesFor = new HashMap<String, HexInfo>();
	private final List<HexData> hexagons = new ArrayList<>();
	private Map<String, BarData> barsFor = new HashMap<>();
	private final ArrayList<TestGroup> allTestClasses = new ArrayList<TestGroup>();
	private LogicInfo uteBar;
	private final Map<String, Map<String, TestResultGroup>> resultGroups = new TreeMap<>();
	private LogicInfo defaultLogic;
	private Map<GroupOfTests, Object> groups = new TreeMap<>();
	private Set<DateListener> buildDateListeners = new HashSet<>();


	public SwimlaneModel(ErrorAccumulator eh, ViewLayout layout) {
		this.eh = eh;
		this.layout = layout;
		
	}
	@Override
	public SolutionCreator startAnalysis(Date startTime) {
		if (currentSolution != null)
			throw new RuntimeException("I think you are running two analyses at the same time ... don't");
		return new SolutionCreator(eh, new SolutionHelper(), constraints);
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
	
	/* should probably implement TRR
	@Override
	public void testsCompleted(Date d) {
		this.testsCompleteTime = d;
	}
	*/

	/*
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
	*/

	@Override
	public void testsStarted(GroupOfTests grp, Date currentDate) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void testCount(GroupOfTests grp, int cnt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void testSuccess(GroupOfTests grp, String testClz, String testFn) {
//		String forClz = test.classUnderTest();
//		BarData bar = barsFor.get(forClz);
//		if (bar == null) {
//			error("the class " + forClz + " was run but did not have a bar defined for it");
//			return;
//		}
//		addResultGroupToBar(bar, test);
//		((BarInfo)bar).passed(forClz);
//		dispatcher.barChanged(bar);
	}

	@Override
	public void testFailure(GroupOfTests grp, String testClz, String testFn, List<String> stack, List<String> expected, List<String> actual) {
//		String forClz = test.classUnderTest();
//		BarData bar = barsFor.get(forClz);
//		if (bar == null) {
//			error("the class " + forClz + " was run but did not have a bar defined for it");
//			return;
//		}
//		addResultGroupToBar(bar, test);
//		((BarInfo)bar).failed(forClz);
//		dispatcher.barChanged(bar);
	}

	@Override
	public void testError(GroupOfTests grp, String testClz, String testFn, List<String> stack) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void testsCompleted(GroupOfTests grp, Date currentDate) {
		// TODO Auto-generated method stub
		
	}

	/* this is presumably a helper for somebody
	private void addResultGroupToBar(BarData bar, TestInfo test) {
		if (!resultGroups.containsKey(bar.getId())) {
			resultGroups.put(bar.getId(), new HashMap<>());
		}
		Map<String, TestResultGroup> rgs = resultGroups.get(bar.getId());
		if (!rgs.containsKey(test.groupName())) {
			rgs.put(new AccumulatedTestResultGroup(test.groupName()).name(), new AccumulatedTestResultGroup(test.groupName()));
		}
		rgs.get(test.groupName()).add(test);
	}
	*/

	public void runAllTests(TestRunner tr) {
		tr.runAll(this, this);
	}

	@Override
	public void allGroups(GroupHandler hdlr) {
		for (GroupOfTests g : groups.keySet())
			hdlr.runGroup(g);
	}

	public class SolutionHelper implements Solution {
		int chex = -1;
		
		@Override
		public void beginHexes() {
			// TODO in update cases, need to start tracking what was already there
			
		}

		@Override
		public void hex(String clzName) {
			HexInfo hi = new HexInfo(clzName);
			layout.addHexagon(SwimlaneModel.this.hexes.size(), hi);
			SwimlaneModel.this.hexes.add(hi);
		}

		@Override
		public void hexesDone() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beginPorts(String hi) {
			for (int i=0;i<hexes.size();i++) {
				if (hexes.get(i).getName().equals(hi)) {
					chex = i;
					return;
				}
			}
			throw new RuntimeException("Protocol error");
		}

		@Override
		public void port(PortLocation loc, String port) {
			if (chex == -1)
				throw new RuntimeException("Protocol error");
			HexInfo hi = hexes.get(chex);
			PortInfo pi = new PortInfo(port, loc);
			hi.addPort(pi);
			layout.addHexagonPort(chex, loc, pi);
		}

		@Override
		public void portsDone(String hi) {
			chex = -1;
		}

		@Override
		public void needsUtilityBar() {
			// TODO Auto-generated method stub
			
		}

		public void analysisDone(Date completeTime) {
			SwimlaneModel.this.analysisDone(completeTime);
		}
	}

}
