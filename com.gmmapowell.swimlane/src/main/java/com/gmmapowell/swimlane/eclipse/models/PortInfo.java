package com.gmmapowell.swimlane.eclipse.models;

import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortInfo implements PortData {
	private final String portClass;
	private final PortLocation loc;

	public PortInfo(String port, PortLocation location) {
		this.portClass = port;
		this.loc = location;
	}
	
	@Override
	public String getName() {
		return portClass;
	}

	public PortLocation getLocation() {
		return loc;
	}

	@Override
	public String toString() {
		return "port[" + portClass + ":" + loc + "]";
	}
}
