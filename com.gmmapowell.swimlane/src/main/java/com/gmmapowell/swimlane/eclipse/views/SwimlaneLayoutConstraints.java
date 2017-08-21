package com.gmmapowell.swimlane.eclipse.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;

public class SwimlaneLayoutConstraints implements LayoutConstrainer {
	public final List<HexagonBackground> bgs = new ArrayList<>();
	public final Map<HexagonBackground, SwimlaneLayoutData> businessBars = new HashMap<>();
	
	public void background(HexagonBackground bg) {
		bgs.add(bg);
	}

	public void businessBarIn(HexagonBackground hc, SwimlaneLayoutData bar) {
		businessBars.put(hc, bar);
	}

}
