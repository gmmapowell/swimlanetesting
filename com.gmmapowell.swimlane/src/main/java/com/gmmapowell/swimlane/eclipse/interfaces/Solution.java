package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

public interface Solution {
	void beginAnalysis();
	void hex(String clzName);

	// for each test class, where it goes is the most recent bar
	// hex - business
	// adapter - adapter
	// acceptance - that acceptance test
	// utility - ute bar
	void testClass(GroupOfTests grp, String clzName, List<String> tests);

	void port(PortLocation loc, String port);
	// for each port
	void adapterAt(String adapter);

	void acceptance(String... hexes);
	void needsUtilityBar();
	void analysisDone(Date completeTime);
}
