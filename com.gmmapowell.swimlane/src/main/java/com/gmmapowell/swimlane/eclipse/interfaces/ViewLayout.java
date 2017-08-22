package com.gmmapowell.swimlane.eclipse.interfaces;

public interface ViewLayout {
	void addHexagon(int pos, BarData hi);
	void addAcceptance(int[] hexes, BarData bar);
	void addHexagonPort(int pos, PortLocation loc);
	void addAdapter(int hex, PortLocation ploc, int aloc, BarData adapter);
	void addUtility(BarData uteData);
}
