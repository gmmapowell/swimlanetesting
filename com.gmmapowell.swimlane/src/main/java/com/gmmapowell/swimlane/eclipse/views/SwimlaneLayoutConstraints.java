package com.gmmapowell.swimlane.eclipse.views;

import java.util.ArrayList;
import java.util.List;

public class SwimlaneLayoutConstraints implements LayoutConstrainer {
	public final List<HexagonBackground> bgs = new ArrayList<>();
	
	public void background(HexagonBackground bg) {
		bgs.add(bg);
	}

}
