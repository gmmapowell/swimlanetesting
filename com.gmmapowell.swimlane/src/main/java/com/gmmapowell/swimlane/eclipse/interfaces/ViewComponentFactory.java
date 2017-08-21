package com.gmmapowell.swimlane.eclipse.interfaces;

import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.views.BarControl;
import com.gmmapowell.swimlane.eclipse.views.HexagonBackground;

public interface ViewComponentFactory {

	HexagonBackground hexagon(Composite parent, int which);
	BarControl bar(Composite view, String name);

}
