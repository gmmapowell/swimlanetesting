package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

public interface Solution {
	void beginHexes();
	void hex(String clzName);
	void hexesDone();
	
	void beginPorts(String hi);
	void port(String hi, PortLocation loc, String port);
	void portsDone(String hi);

	void needsUtilityBar();
	void analysisDone(Date completeTime);
}
