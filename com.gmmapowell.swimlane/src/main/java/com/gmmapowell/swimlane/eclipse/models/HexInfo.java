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
	private final List<PortData> ports = new ArrayList<PortData>();
	private LogicInfo bar;
	
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
		return bar;
	}

	@Override
	public List<PortData> getPorts() {
		return ports;
	}

	public BarInfo ensureBar() {
		if (bar == null)
			bar = new LogicInfo(id +".logic");
		return bar;
	}

	public void setPortLocation(Class<?> port, PortLocation loc) {
		PortInfo pi = requirePort(port);
		for (PortData pd : ports) {
			if (pd != pi && pd.getLocation() == loc) {
				acc.error("Cannot place multiple ports in the " + loc);
				return;
			}
		}
		pi.setLocation(loc);
	}

	public PortInfo requirePort(Class<?> port) {
		PortInfo pi = hasPort(port);
		if (pi != null)
			return pi;
		pi = new PortInfo(acc, port);
		ports.add(pi);
		return pi;
	}

	protected PortInfo hasPort(Class<?> port) {
		String name = port.getName();
		for (PortData pd : ports) {
			if (pd.getName().equals(name))
				return (PortInfo) pd;
		}
		return null;
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
