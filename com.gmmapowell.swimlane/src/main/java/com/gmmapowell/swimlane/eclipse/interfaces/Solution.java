package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

public interface Solution {
	void beginHexes();
	void hex(HexData hi);
	void hexesDone();
	
	void beginPorts(HexData hi);
	void port(HexData hi, PortData port);
	void portsDone(HexData hi);

	void needsUtilityBar();
	void analysisDone(Date completeTime);
}
