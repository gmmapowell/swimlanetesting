package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class HexInfo implements HexData {
	private final List<PortData> ports = new ArrayList<PortData>();
	private LogicInfo bar;
	
	@Override
	public BarData getBar() {
		return bar;
	}

	@Override
	public List<PortData> getPorts() {
		return ports;
	}

	public void setBar(LogicInfo bar) {
		if (this.bar != null)
			throw new RuntimeException("Cannot overwrite a created bar");
		this.bar = bar;
	}

//	public BarInfo ensureBar() {
//		if (bar == null)
//			bar = new LogicInfo(id, id +".logic");
//		return bar;
//	}

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
}
