package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultGroup;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;

public class HexagonAccumulator implements ErrorAccumulator, AnalysisAccumulator, DataCentral, TestResultReporter {
	private Date buildTime;
	private Date testsCompleteTime;
	private final Map<String, Acceptance> compileAcceptances = new TreeMap<String, Acceptance>();
	private List<BarData> acceptances = new ArrayList<>();
	private final TotalOrder hexorder = new TotalOrder();
	private List<String> order;
	private final Map<String, HexInfo> hexesFor = new HashMap<String, HexInfo>();
	private final List<HexData> hexagons = new ArrayList<>();
	private Set<String> errors = new TreeSet<>();
	private Map<String, BarData> barsFor = new HashMap<>();
	private final ArrayList<TestGroup> allTestClasses = new ArrayList<TestGroup>();
	private LogicInfo uteBar;
	private Map<Class<?>, PortLocation> adapterLocations = new HashMap<>();
	private Map<Class<?>, Class<?>> adapterPort = new HashMap<>();
	private Map<Class<?>, String> portHex = new HashMap<>();
	private Map<Class<?>, Adapter> adapters = new HashMap<>();
	private final Map<String, Map<String, TestResultGroup>> resultGroups = new TreeMap<>();
	private LogicInfo defaultLogic;
	private Map<GroupOfTests, Object> groups = new TreeMap<>();
	private Set<DateListener> buildDateListeners = new HashSet<>();
	

	@Override
	public void startAnalysis(Date startTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void haveTestClass(GroupOfTests grp, String clzName, TestRole role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void analysisComplete(Date completeTime) {
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

	/* TDA
	@Override
	public Date getBuildTime() {
		return this.buildTime;
	}
	*/
	
	/* should probably implement TRR
	@Override
	public void testsCompleted(Date d) {
		this.testsCompleteTime = d;
	}
	*/

	/* should not have any getters - we're TDA now
	@Override
	public Date getTestCompleteTime() {
		return testsCompleteTime;
	}

	@Override
	public int getHexCount() {
		return hexorder.count();
	}

	@Override
	public List<BarData> getAcceptanceTests() {
		return acceptances;
	}
	
	@Override
	public List<TestGroup> getAllTestGroups() {
		return allTestClasses;
	}
	*/
	
	/* should probably implement AA
	@Override
	public void acceptance(TestGroup grp, Class<?> tc, List<Class<?>> hexes) {
		if (hexes == null || hexes.isEmpty()) {
			this.hexorder.haveDefault();
		} else
			this.hexorder.addAll(hexes);
		for (Class<?> h : hexes)
			inithex(h);
		List<String> names = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : hexes) {
			names.add(c.getName());
			inithex(c);
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
		barsFor.put(tc.getName(), ca);
		collectCase(ca, grp, tc);
	}

	@Override
	public void logic(TestGroup grp, Class<?> tc, Class<?> hex) {
		BarInfo bar;
		if (hex == null) {
			if (defaultLogic == null)
				defaultLogic = new LogicInfo(null, "-default-");
			bar = defaultLogic;
		} else {
			HexInfo hi = inithex(hex);
			bar = hi.ensureBar();
		}
		barsFor.put(tc.getName(), bar);
		collectCase(bar, grp, tc);
	}

	@Override
	public void adapter(TestGroup grp, Class<?> tc, Class<?> hex, Class<?> port, Class<?> adapter) {
		if (port != null) {
			if (adapterPort.containsKey(adapter)) {
				if (!adapterPort.get(adapter).equals(port))
					error("cannot bind adapter " + adapter.getName() + " to both " + port.getName() + " and " + adapterPort.get(adapter).getName());
			} else
				adapterPort.put(adapter, port);
			if (hex != null) {
				if (portHex.containsKey(port)) {
					if (!portHex.get(port).equals(hex.getName()))
						error("cannot bind port " + port.getName() + " to both " + hex.getName() + " and " + portHex.get(port));
				} else
					portHex.put(port, hex.getName());
			}
		}
		if (hex != null) {
			inithex(hex);
		}
		Adapter bar = adapters.get(adapter);
		if (bar == null) {
			bar = new Adapter(adapter);
			adapters.put(adapter, bar);
		}
		barsFor.put(tc.getName(), bar);
		collectCase(bar, grp, tc);
	}
	
	@Override
	public void utility(TestGroup grp, Class<?> tc) {
		if (uteBar == null)
			uteBar = new LogicInfo(null, "utility");
		barsFor.put(tc.getName(), uteBar);
		collectCase(uteBar, grp, tc);
	}

	@Override
	public void portLocation(Class<?> adapter, PortLocation loc) {
		if (adapterLocations.containsKey(adapter)) {
			if (!adapterLocations.get(adapter).equals(loc))
				error("cannot assign locations " + adapterLocations.get(adapter) + " and " + loc + " to adapter " + adapter.getName());
			return;
		}
		adapterLocations.put(adapter, loc);
	}
	*/

	/* Don't know what this is helping, but for now ...
	private HexInfo inithex(Class<?> hex) {
		if (hex == null)
			throw new RuntimeException("Shouldn't do that");
		String name = hex.getName();
		if (hexesFor.containsKey(name)) {
			return hexesFor.get(name);
		}
		this.hexorder.add(name);
		HexInfo hi = new HexInfo(this, name);
		hexesFor.put(name, hi);
		return hi;
	}
	*/

	/* Part of AA ...
	@Override
	public void analysisComplete() {
		if (defaultLogic != null)
			this.hexorder.haveDefault();
		else if (!adapterPort.isEmpty() && portHex.isEmpty())
			this.hexorder.haveDefault();
		this.hexorder.ensureTotalOrdering(errors);
		TreeMap<String, Acceptance> tmp = new TreeMap<String, Acceptance>();
		order = this.hexorder.bestOrdering(errors);
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

		if (order.contains("-default-")) {
			HexInfo hi = new HexInfo(this, "-default-");
			hexesFor.put(hi.getId(), hi);
			if (defaultLogic != null)
				hi.setBar(defaultLogic);
		} else if (defaultLogic != null) {
			for (String c : defaultLogic.classesUnderTest())
				error("cannot use @BusinessLogic with default hexagon in " + c + " since there are multiple hexagons");
		}
		
		// define links from all ports to default hex if appropriate
		if (portHex.isEmpty()) {
			for (Class<?> q : adapterPort.values()) {
				portHex.put(q, "-default-");
			}
		}
		
		// bind ports to hexes
		Map<Class<?>, PortInfo> ports = new HashMap<>();
		for (Entry<Class<?>, String> q : portHex.entrySet()) {
			HexInfo hex = hexesFor.get(q.getValue());
			if (hex == null)
				throw new RuntimeException("Didn't find hex for " + q.getValue());
			PortInfo pi = hex.requirePort(q.getKey());
			ports.put(q.getKey(), pi);
		}
		
		// bind adapters to ports
		for (Entry<Class<?>, Class<?>> q : adapterPort.entrySet()) {
			Class<?> adClz = q.getKey();
			Adapter adapter = adapters.remove(adClz);
			Class<?> port = q.getValue();
			PortInfo pi = ports.get(port);
			if (pi == null) {
				error("port " + port.getName() + " was not bound to a hexagon");
				continue;
			}
			pi.setAdapter(adClz, adapter);
			PortLocation loc = adapterLocations.get(adClz);
			if (loc != null) {
				HexInfo hi = hexesFor.get(portHex.get(port));
				hi.setPortLocation(port, loc);
			}
		}
		
		// assert we used all the adapters
		for (Class<?> rem : adapters.keySet()) {
			error("did not bind adapter " + rem.getName() + " to a port");
		}
		
		// Wrap up
		for (String s : order) {
			HexInfo hex = hexesFor.get(s);
			hex.analysisComplete();
			hexagons.add(hex);
		}
	}

	protected void collectCase(BarInfo ca, TestGroup grp, Class<?> tc) {
		if (!allTestClasses.contains(grp))
			allTestClasses.add(grp);
		grp.addTest(tc.getName());
		ca.addCase(tc);
	}
	*/
	
	/* TDA no getters
	@Override
	public List<HexData> getHexagons() {
		return hexagons;
	}

	@Override
	public BarData getUtilityBar() {
		return uteBar;
	}
	*/

	/* These errors relate to static analysis errors, such as hexagons in the wrong order.
	 * These methods are here because we override the (mocked) interface Accumulator and its tests demand that errors be reported
	 */
	@Override
	public void error(String msg) {
		errors.add(msg);
	}

	/* TDA
	@Override
	public Set<String> getErrors() {
		return errors;
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

	/* TDA
	@Override
	public Collection<TestResultGroup> getTestResultsFor(String barId) {
		if (barId == null)
			throw new RuntimeException("BarId cannot be null");
		Map<String, TestResultGroup> ret = resultGroups.get(barId);
		if (ret == null)
			return new HashSet<>();
		return ret.values();
	}
	*/

	// TDA: this should do much more of the work
	public void runAllTests(TestRunner tr) {
		tr.runAll(this, this);
	}

	@Override
	public void allGroups(GroupHandler hdlr) {
		for (GroupOfTests g : groups.keySet())
			hdlr.runGroup(g);
	}
}
