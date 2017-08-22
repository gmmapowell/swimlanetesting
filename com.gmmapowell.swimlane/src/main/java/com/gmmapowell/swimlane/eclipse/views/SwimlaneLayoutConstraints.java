package com.gmmapowell.swimlane.eclipse.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;
import com.gmmapowell.swimlane.eclipse.utils.ReverseStringOrdering;

public class SwimlaneLayoutConstraints implements LayoutConstrainer {
	public final TreeMap<String, AcceptanceBarLayout> acceptances = new TreeMap<>(new ReverseStringOrdering());
	public final List<HexagonBackground> bgs = new ArrayList<>();
	public final Map<HexagonBackground, SwimlaneLayoutData> businessBars = new HashMap<>();
	
	public void background(HexagonBackground bg) {
		bgs.add(bg);
	}

	public void businessBarIn(HexagonBackground hc, SwimlaneLayoutData bar) {
		businessBars.put(hc, bar);
	}

	public void acceptanceCalled(String mask, AcceptanceBarLayout layout) {
		acceptances.put(mask, layout);
	}

}
