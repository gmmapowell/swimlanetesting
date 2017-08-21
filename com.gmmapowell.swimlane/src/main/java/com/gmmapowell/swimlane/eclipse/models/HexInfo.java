package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.HasABar;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public class HexInfo implements HexData, HasABar {
	private final String name;
	private final List<PortData> ports = new ArrayList<PortData>();
	private final Set<BarDataListener> lsnrs = new HashSet<>();
	
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
	public void addBusinessLogicListener(BarDataListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public void clearGroup(GroupOfTests grp) {
		for (BarDataListener lsnr : lsnrs)
			lsnr.clearGroup(grp);
	}

	@Override
	public void testCompleted(TestCaseInfo ti) {
		for (BarDataListener lsnr : lsnrs)
			lsnr.testCompleted(ti);
	}

	@Override
	public String toString() {
		return "hex[" + name + "]";
	}
}
