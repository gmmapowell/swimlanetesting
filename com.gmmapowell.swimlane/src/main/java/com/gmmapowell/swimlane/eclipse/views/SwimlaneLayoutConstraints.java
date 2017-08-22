package com.gmmapowell.swimlane.eclipse.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;
import com.gmmapowell.swimlane.eclipse.utils.ReverseStringOrdering;

public class SwimlaneLayoutConstraints implements LayoutConstrainer {
	public final TreeMap<String, AcceptanceBarLayout> acceptances = new TreeMap<>(new ReverseStringOrdering());
	public final List<HexagonBackground> bgs = new ArrayList<>();
	public final Map<HexagonBackground, SwimlaneLayoutData> businessBars = new HashMap<>();
	public final Map<Integer, Map<PortLocation, PortControl>> ports = new HashMap<>();
	public final Map<Integer, Map<PortLocation, List<SwimlaneLayoutData>>> adapters = new HashMap<>();
	public UtilityBarLayout utility;
	
	public void background(HexagonBackground bg) {
		bgs.add(bg);
	}

	public void businessBarIn(HexagonBackground hc, SwimlaneLayoutData bar) {
		businessBars.put(hc, bar);
	}
	
	public void port(int hex, PortLocation loc, PortControl portControl) {
		if (!ports.containsKey(hex))
			ports.put(hex, new HashMap<>());
		Map<PortLocation, PortControl> map = ports.get(hex);
		map.put(loc, portControl);
	}
	
	public void adapter(int hex, PortLocation ploc, int aloc, AdapterBarLayout adapterBarLayout) {
		if (!adapters.containsKey(hex))
			adapters.put(hex, new HashMap<>());
		Map<PortLocation, List<SwimlaneLayoutData>> map = adapters.get(hex);
		if (!map.containsKey(ploc))
			map.put(ploc, new ArrayList<>());
		List<SwimlaneLayoutData> list = map.get(ploc);
		while (list.size() <= aloc)
			list.add(null);
		list.set(aloc, adapterBarLayout);
	}

	public void acceptanceCalled(String mask, AcceptanceBarLayout layout) {
		acceptances.put(mask, layout);
	}

	public void utility(UtilityBarLayout utility) {
		this.utility = utility;
	}
}
