package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Composite;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewComponentFactory;

public class SwimlaneViewComponentFactory implements ViewComponentFactory {
	public HexagonBackground hexagon(Composite parent, int which) {
		HexagonBackground hex = new HexagonBackground(parent, which /*, hexn, hexId, hexModel.getBar(), hexModel.getPorts()*/);

		/*
		// Move the hex bar above any backgrounds
		// The background is automatically added at the end
		for (Control c : view.getChildren()) {
			String type = (String) c.getData("com.gmmapowell.swimlane.type");
			if (type != null && type.equals("hexbg")) {
				hex.getBar().moveAbove(c);
				for (PortControl pc : hex.getPorts())
					pc.getCanvas().moveAbove(c);
				break;
			}
		}
		view.layout();
		*/
		return hex;
	}

	@Override
	public BarControl bar(Composite view, String name) {
		BarControl ret = new BarControl(view, name);
		return ret;
	}

	
}
