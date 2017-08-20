package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class HexInfo implements HexData {
	private final String name;
	private final List<PortData> ports = new ArrayList<PortData>();
	
	public HexInfo(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public List<PortData> getPorts() {
		return ports;
	}

	public void addPort(PortData portInfo) {
		ports.add(portInfo);
	}

	protected PortData getPort(String port) {
		for (PortData pd : ports) {
			if (pd.getName().equals(port))
				return (PortInfo) pd;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "hex[" + name + "]";
	}
}
