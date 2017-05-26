package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Collection;

public interface HexData {
	String getId();
	BarData getBar();
	Collection<PortData> getPorts();
}
