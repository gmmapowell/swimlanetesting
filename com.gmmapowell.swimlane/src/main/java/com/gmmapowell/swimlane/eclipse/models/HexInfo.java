package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class HexInfo implements HexData {
	private final Accumulator acc;
	private final String id;
	private List<PortData> ports = new ArrayList<PortData>();
	
	public HexInfo(Accumulator acc, String id) {
		this.acc = acc;
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

	public PortInfo requirePort(Class<?> port) {
		String name = port.getName();
		for (PortData pd : ports) {
			if (pd.getName().equals(name))
				return (PortInfo) pd;
		}
		PortInfo pi = new PortInfo(acc, port);
		ports.add(pi);
		return pi;
	}

	public void analysisComplete() {
		List<PortLocation> locs = new ArrayList<PortLocation>();
		for (PortLocation p : PortLocation.values())
			locs.add(p);
		for (PortData p : ports) {
			if (p.getLocation() != null)
				locs.remove(p.getLocation());
		}
		for (PortData p : ports) {
			if (p.getLocation() == null)
				((PortInfo)p).setLocation(locs.remove(0));
		}
	}

}
