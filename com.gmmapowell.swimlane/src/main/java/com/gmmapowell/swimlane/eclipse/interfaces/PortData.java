package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface PortData {
	String getName();
	PortLocation getLocation();
	List<BarData> getAdapters();
}
