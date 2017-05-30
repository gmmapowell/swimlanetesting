package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortInfo implements PortData {
	private final Accumulator acc;
	private final Class<?> portClass;
	private final List<Adapter> bars = new ArrayList<>();
	private PortLocation loc;

	public PortInfo(Accumulator acc, Class<?> port) {
		this.acc = acc;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<BarData> getAdapters() {
		return (List)bars;
	}

	public void setLocation(PortLocation loc) {
		if (this.loc != null && this.loc != loc) {
			acc.error("Cannot specify multiple locations for port " + getName());
			return;
		}
		this.loc = loc;
	}

	public void setAdapter(Class<?> cls, Adapter adapter) {
		for (Adapter ai : bars) {
			if (ai.forAdapter(cls)) {
				acc.error("Duplicate adapter for " + cls.getName());
				return;
			}
		}
		bars.add(adapter);
	}

}
