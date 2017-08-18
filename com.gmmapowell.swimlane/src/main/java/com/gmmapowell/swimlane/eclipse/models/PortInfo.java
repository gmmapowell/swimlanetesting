package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortInfo implements PortData {
	private final String portClass;
	private final PortLocation loc;
	private final List<Adapter> bars = new ArrayList<>();

	public PortInfo(String port, PortLocation location) {
		this.portClass = port;
		this.loc = location;
	}
	
	@Override
	public String getName() {
		return portClass;
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

	public void setAdapter(Class<?> cls, Adapter adapter) {
//		for (Adapter ai : bars) {
//			if (ai.forAdapter(cls)) {
//				acc.error("Duplicate adapter for " + cls.getName());
//				return;
//			}
//		}
//		bars.add(adapter);
	}

}
