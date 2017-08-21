package com.gmmapowell.swimlane.eclipse.interfaces;

import com.gmmapowell.swimlane.eclipse.views.SwimlaneLayoutConstraints;

public interface SwimlaneLayoutData {

	void constrain(SwimlaneLayoutConstraints constraints);

	void layout(int xpos, int ypos, int xmax, int ymax);

}
