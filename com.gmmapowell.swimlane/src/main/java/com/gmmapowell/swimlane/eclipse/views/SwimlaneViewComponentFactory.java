package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewComponentFactory;

public class SwimlaneViewComponentFactory implements ViewComponentFactory {
	public HexagonBackground hexagon(Composite parent, int which) {
		HexagonBackground hex = new HexagonBackground(parent, which);
		return hex;
	}

	@Override
	public PortControl port(Composite parent, int hex, PortLocation loc, PortData port) {
		PortControl pc = new PortControl(parent, hex, loc, port);
		return pc;
	}

	@Override
	public BarControl bar(Composite view, String name) {
		BarControl ret = new BarControl(view, name);
		return ret;
	}
}
