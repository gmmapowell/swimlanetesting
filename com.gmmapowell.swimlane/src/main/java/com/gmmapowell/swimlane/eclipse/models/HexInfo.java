package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class HexInfo implements HexData {
	private final String id;
	private List<PortData> ports = new ArrayList<PortData>();
	
	public HexInfo(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public BarData getBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PortData> getPorts() {
		return ports;
	}

	public void requirePort(Class<?> port) {
		PortInfo pi = new PortInfo(port);
		ports.add(pi);
	}

}