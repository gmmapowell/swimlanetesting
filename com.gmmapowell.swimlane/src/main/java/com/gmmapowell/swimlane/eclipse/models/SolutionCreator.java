package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gmmapowell.swimlane.eclipse.analyzer.BusinessRole;
import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;

// should only be the AnalysisAccumulator ...
public class SolutionCreator implements AnalysisAccumulator {
	private final ErrorAccumulator eh;
	private final Solution solution;

	private Map<GroupOfTests, AllConstraints> constraints;

	private List<String> hexOrdering = new ArrayList<>();
	private Map<String, HexTracker> hexes = new TreeMap<>();
	private boolean wantUte;

	public SolutionCreator(ErrorAccumulator eh, Solution sol, Map<GroupOfTests, AllConstraints> constraints) {
		this.eh = eh;
		solution = sol;
		this.constraints = constraints;
	}
	
	@Override
	public void clean(GroupOfTests grp) {
		constraints.remove(grp);
	}

	@Override
	public void haveTestClass(GroupOfTests grp, String clzName, TestRole role, List<String> tests) {
		if (!constraints.containsKey(grp))
			constraints.put(grp, new AllConstraints());
		AllConstraints c = constraints.get(grp);
		if (role instanceof AcceptanceRole)
			collectAcceptanceInfo(c, (AcceptanceRole)role);
		else if (role instanceof AdapterRole)
			collectAdapterInfo(c, (AdapterRole)role);
		else if (role instanceof UtilityRole)
			collectUtilityInfo(c, (UtilityRole)role);
		else if (role instanceof BusinessRole)
			collectBusinessLogicInfo(c, (BusinessRole)role, grp, clzName, tests);
		else
			eh.error("cannot handle " + role.getClass());
	}

	private void collectAcceptanceInfo(AllConstraints c, AcceptanceRole role) {
		c.acceptances.acase(role.getHexes());
	}

	private void collectBusinessLogicInfo(AllConstraints c, BusinessRole role, GroupOfTests grp, String testClass, List<String> tests) {
		String s = role.getHex();
		TestCaseTracker tc = new TestCaseTracker(grp, testClass, tests);
		if (s == null) {
			c.classesInBusinessDefault.add(testClass);
			c.defaultTracker.add(tc);
		} else {
			HexTracker curr = null;
			for (HexTracker ht : c.businessHexes) {
				if (ht.name.equals(s)) {
					curr = ht;
					break;
				}
			}
			if (curr == null) {
				curr = new HexTracker(s);
				c.businessHexes.add(curr);
			}
			curr.add(tc);
		}
	}

	private void collectAdapterInfo(AllConstraints c, AdapterRole role) {
		String adapter = role.getAdapter();		
		if (!c.adapters.containsKey(adapter))
			c.adapters.put(adapter, new AdapterConstraints());
		AdapterConstraints cxt = c.adapters.get(adapter);
		cxt.addHex(role.getHex());
		// could add test to adapter (if we passed it in) if that would be interesting
		String port = role.getPort();
		if (port != null) {
			cxt.addPort(port);
			if (!c.ports.containsKey(port))
				c.ports.put(port, new PortConstraints());
			PortConstraints pc = c.ports.get(port);
			pc.addHex(role.getHex());
			pc.addLocation(role.getLocation());
			pc.adapters.add(adapter);
		}
	}

	private void collectUtilityInfo(AllConstraints c, UtilityRole role) {
		c.hasUtility = true;
	}

	@Override
	public void analysisComplete(Date completeTime) {
		// I think this should probably collect its info in a one-time-use structure
		// rather than fields ...
		scanForHexes();
		addBusinessLogicTestsToHexes();
		fleshOutPorts();
		fleshOutAdapters();
		figureUtilityBar();
		announceResults(completeTime);
	}

	private void scanForHexes() {
		// List because order *is* important
//		boolean needSomething = false;
//		List<String> hexes = new ArrayList<String>();
		TotalOrder hexorder = new TotalOrder();
		List<String> defaultLogic = new ArrayList<>();
		
		// TODO: I think I'm looping wrongly about this; I think
		// the bestordering should be called after we've looped over all
		// the acceptance tests, so:
		//   loop on constraints, collect bestorder
		//   figure the best ordering & update hexes
		//   then loop to do adapters
		for (AllConstraints ac : constraints.values()) {
			for (List<String> s : ac.acceptances.allHexes)
				if (s.isEmpty())
					hexorder.haveDefault();
				else
					hexorder.addAll(s);
//			if (defaultLogic != null)
//				this.hexorder.haveDefault();
//			else if (!adapterPort.isEmpty() && portHex.isEmpty())
//				this.hexorder.haveDefault();
//			this.hexorder.ensureTotalOrdering(errors);
//			TreeMap<String, Acceptance> tmp = new TreeMap<String, Acceptance>();
//			for (Acceptance a : compileAcceptances.values()) {
//				a.setMarks(order);
//				// Handle an error case where because of inconsistent hex definitions, we have two
//				// different acceptance tests that think they represent the same pattern (i.e. we can't distinguish two 1s in the name in different orders)
//				// Merge these into a single test
//				Acceptance prev = tmp.get(a.getId());
//				if (prev != null) {
//					prev.merge(a);
//				} else {
//					// This is the normal non-error case
//					tmp.put(a.getId(), a);
//				}
//			}
//			// Because we want to sort 111, 110, 101, 100, 011 ... reverse the default sorted list by adding each item on the front
//			for (Acceptance a : tmp.values()) {
//				acceptances.add(0, a);
//				for (String c : a.classesUnderTest())
//					barsFor.put(c, a);
//			}

			defaultLogic.addAll(ac.classesInBusinessDefault);
			for (HexTracker ht : ac.businessHexes) {
				hexorder.add(ht.name); // don't use addAll because that implies ordered
			}

			for (Entry<String, AdapterConstraints> e : ac.adapters.entrySet()) {
				AdapterConstraints c = e.getValue();
				if (c.hexes.isEmpty())
					hexorder.haveDefault();
				else
					hexorder.add(c.hexes.iterator().next());
			}
		}
		if (!defaultLogic.isEmpty())
			hexorder.haveDefault();
		Set<String> errs = new TreeSet<>();
		hexorder.ensureTotalOrdering(errs);
		for (String s : errs)
			eh.error(s);
		Set<String> errs2 = new TreeSet<>();
		List<String> order = hexorder.bestOrdering(errs2);
		if (errs.isEmpty())
			for (String s : errs2)
				eh.error(s);
		if (!defaultLogic.isEmpty() && order.size() > 1) {
			for (String c : defaultLogic)
				eh.error("cannot use @BusinessLogic with default hexagon in " + c + " since there are multiple hexagons");
		}
		for (String s : order) {
			addHex(s);
		}
	}

	private void addBusinessLogicTestsToHexes() {
		for (AllConstraints ac : constraints.values()) {
			for (TestCaseTracker k : ac.defaultTracker.tests) {
				this.hexes.get(this.hexOrdering.get(0)).tests.add(k);
			}
			for (HexTracker ht : ac.businessHexes) {
				for (TestCaseTracker k : ht.tests) {
					this.hexes.get(ht.name).tests.add(k);
				}
			}
		}
		
	}

	private void fleshOutPorts() {
		// for each hex, create a map of available locations so that we can assign the defaults later
		Map<String, List<PortLocation>> available = new HashMap<>();
		for (String hex : hexOrdering)
			available.put(hex, new ArrayList<>(Arrays.asList(PortLocation.values())));

		// first gather info on all the hexes' port locations
		Map<HexLoc, Set<String>> locations = new HashMap<>();
		for (AllConstraints ac : constraints.values()) {
			for (Entry<String, PortConstraints> e : ac.ports.entrySet()) {
				PortConstraints c = e.getValue();
				String hex = c.getHexName();
				if (hex == null) {
					eh.error("port " + e.getKey() + " was not bound to a hexagon");
					continue;
				}
				PortLocation loc = c.getLocation();
				if (loc != null) {
					HexLoc hl = new HexLoc(hex, loc);
					if (!locations.containsKey(hl)) {
						locations.put(hl, new TreeSet<>());
						available.get(hex).remove(loc);
					}
					locations.get(hl).add(e.getKey());
				}
			}
		}
		
		// Now invert that to get a map of ports to locations, noting any clashes
		Map<String, HexLoc> portsAreAt = new HashMap<>();
		for (Entry<HexLoc, Set<String>> e : locations.entrySet()) {
			if (e.getValue().size() > 1) {
				StringBuilder sb = new StringBuilder("ports");
				String sep = " ";
				int sz = e.getValue().size();
				Iterator<String> it = e.getValue().iterator();
				for (int i=0;i<sz;i++) {
					sb.append(sep);
					sb.append(it.next());
					if (i == sz-2)
						sep = " and ";
					else
						sep = ", ";
				}
				sb.append(" cannot all be in ");
				sb.append(e.getKey().loc);
				eh.error(sb.toString());
			}
			String first = e.getValue().iterator().next();
			if (portsAreAt.containsKey(first))
				eh.error("that's two places that both want " + first); // work through this with tests
			else
				portsAreAt.put(first, e.getKey());
		}
		
		// Now build the actual ports, inasmuch as they don't already exist
		for (AllConstraints ac : constraints.values()) {
			for (Entry<String, PortConstraints> e : ac.ports.entrySet()) {
				HexLoc hl = portsAreAt.get(e.getKey());
				PortConstraints c = e.getValue();
				String hn;
				if (c.locations.size() > 1) {
					StringBuilder sb = new StringBuilder("port " + e.getKey() + " cannot be in");
					int sz = c.locations.size();
					Iterator<PortLocation> p = c.locations.iterator();
					for (int j=0;j<sz;j++) {
						sb.append(" ");
						sb.append(p.next());
						if (j == sz-2)
							sb.append(" and");
					}
					eh.error(sb.toString());
				}
				if (hl == null) {
					hn = c.getHexName();
					if (hn == null)
						continue;
					List<PortLocation> options = available.get(hn);
					if (options.isEmpty()) {
						eh.error("no free locations to put port " + e.getKey() + " on hex " + hn);
						continue;
					}
					hl = new HexLoc(hn, options.remove(0));
				} else
					hn = hl.hex;
				HexTracker hi = hexes.get(hn);
				PortTracker pi = hi.getPort(e.getKey());
				if (pi == null) {
					pi = new PortTracker(e.getKey(), hl.loc);
					hi.addPort(pi);
					
					for (String at : c.adapters) {
						pi.adapters.add(new AdapterTracker(at));
					}
				}
			}
		}
	}

	private void fleshOutAdapters() {
		for (AllConstraints ac : constraints.values()) {
			for (Entry<String, AdapterConstraints> e : ac.adapters.entrySet()) {
				AdapterConstraints c = e.getValue();
				if (!c.hexes.isEmpty()) {
					Iterator<String> it = c.hexes.iterator();
					it.next(); // one is to be expected ...
					// TODO: I think we should tell somebody this ...
					if (it.hasNext()) { // multiple hexes
						StringBuilder sb = new StringBuilder("duh");
						while (it.hasNext())
							sb.append(it.next());
						eh.error(sb.toString());
					}
				}
				if (c.ports.isEmpty())
					eh.error("did not bind adapter " + e.getKey() + " to a port");
			}
		}
	}

	private void figureUtilityBar() {
		wantUte = false;
		for (AllConstraints ac : constraints.values()) {
			wantUte |= ac.hasUtility;
		}
	}

	private void announceResults(Date completeTime) {
		if (solution != null) {
			solution.beginAnalysis();
			for (String s : hexOrdering) {
				solution.hex(s);
				HexTracker hi = hexes.get(s);
				for (TestCaseTracker tc : hi.tests)
					solution.testClass(tc.grp, tc.testClass, tc.tests);
				for (PortTracker p : hi.getPorts()) {
					solution.port(p.loc, p.name);
					for (AdapterTracker a : p.adapters) {
						solution.adapter(a.name);
					}
				}
			}
			
			if (wantUte)
				solution.needsUtilityBar();
			
			solution.analysisDone(completeTime);
		}
	}

	private HexTracker addHex(String s) {
		HexTracker hi = new HexTracker(s);
		this.hexes.put(s, hi);
		this.hexOrdering.add(s);
		return hi;
	}

	public class AllConstraints {
		// acceptance tests
		AcceptanceConstraints acceptances = new AcceptanceConstraints();
		// business logic references hexes ...
		List<HexTracker> businessHexes = new ArrayList<>();
		// and cases that don't reference a hex
		HexTracker defaultTracker = new HexTracker("");
		List<String> classesInBusinessDefault = new ArrayList<>();
		// adapter class name -> constraints
		Map<String, AdapterConstraints> adapters = new TreeMap<>();
		// port class name -> constraints
		Map<String, PortConstraints> ports = new TreeMap<>();
		// does it have any utility tests?
		public boolean hasUtility;
	}

	public class AcceptanceConstraints {
		private final List<List<String>> allHexes = new ArrayList<>();

		public void acase(List<String> hexes) {
			allHexes.add(hexes);
		}
	}

	public class AdapterConstraints {
		public Set<String> hexes = new TreeSet<>();
		public Set<String> ports = new TreeSet<>();

		public void addHex(String hex) {
			if (hex != null)
				hexes.add(hex);
		}

		public void addPort(String port) {
			if (port != null)
				ports.add(port);
		}
	}

	public class PortConstraints {
		public Set<String> chexes = new TreeSet<>();
		public Set<PortLocation> locations = new TreeSet<>();
		public Set<String> adapters = new TreeSet<>();

		public void addHex(String hex) {
			if (hex != null)
				chexes.add(hex);
		}

		public PortLocation getLocation() {
			if (locations.isEmpty())
				return null;
			return locations.iterator().next();
		}

		public String getHexName() {
			if (chexes.isEmpty() && hexes.size() > 1)
				return null; // can't be done
			if (!chexes.isEmpty()) {
				return chexes.iterator().next();
			} else {
				return "";
			}
		}

		public void addLocation(PortLocation location) {
			if (location != null)
				locations.add(location);
		}
		
		public void addAdapter(String adapterName) {
			this.adapters.add(adapterName);
		}

	}

	public class HexLoc {
		private final String hex;
		private final PortLocation loc;

		public HexLoc(String hex, PortLocation loc) {
			this.hex = hex;
			this.loc = loc;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof HexLoc))
				return false;
			HexLoc other = (HexLoc) obj;
			return this.hex.equals(other.hex) && this.loc.equals(other.loc);
		}
		
		@Override
		public int hashCode() {
			return hex.hashCode() ^ loc.hashCode();
		}
		
		@Override
		public String toString() {
			return "HexLoc[" + hex +":"+loc+"]";
		}
	}

	public static class HexTracker {
		private final String name;
		private final List<PortTracker> ports = new ArrayList<>();
		private final List<TestCaseTracker> tests = new ArrayList<>();
		
		public HexTracker(String name) {
			this.name = name;
		}
		
		public void add(TestCaseTracker tc) {
			tests.add(tc);
		}

		public String getName() {
			return name;
		}

		public List<PortTracker> getPorts() {
			return ports;
		}

		public void addPort(PortTracker portInfo) {
			ports.add(portInfo);
		}

		protected PortTracker getPort(String port) {
			for (PortTracker pd : ports) {
				if (pd.name.equals(port))
					return pd;
			}
			return null;
		}
		
		@Override
		public String toString() {
			return "hex[" + name + "]";
		}
	}

	public class TestCaseTracker {
		private final GroupOfTests grp;
		private final String testClass;
		private final List<String> tests;

		public TestCaseTracker(GroupOfTests grp, String testClass, List<String> tests) {
			this.grp = grp;
			this.testClass = testClass;
			this.tests = tests;
		}

	}

	public static class PortTracker {
		private final String name;
		private final PortLocation loc;
		private final List<AdapterTracker> adapters = new ArrayList<>();

		public PortTracker(String port, PortLocation location) {
			this.name = port;
			this.loc = location;
		}
	}

	public static class AdapterTracker {
		private String name;

		public AdapterTracker(String at) {
			this.name = at;
		}
	}

}
