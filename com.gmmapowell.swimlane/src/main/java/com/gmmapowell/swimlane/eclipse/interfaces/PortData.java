package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Collection;

public interface PortData {
	String getName();
	PortLocation getLocation();
	Collection<BarData> getAdapters();
}
