package com.gmmapowell.swimlane.eclipse.models;

import java.util.Collection;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortInfo implements PortData {
	private final Class<?> portClass;
	private PortLocation loc;

	public PortInfo(Class<?> port) {
		this.portClass = port;
	}
	
	@Override
	public String getName() {
		return portClass.getName();
	}

	@Override
	public PortLocation getLocation() {
		return loc;
	}

	@Override
	public Collection<BarData> getAdapters() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLocation(PortLocation loc) {
		this.loc = loc;
	}

}
