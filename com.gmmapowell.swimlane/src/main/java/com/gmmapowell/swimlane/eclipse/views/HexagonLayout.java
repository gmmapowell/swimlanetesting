package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class HexagonLayout extends Layout {

	public HexagonLayout(HexagonDataModel model) {
	}

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		return new Point(wHint, hHint);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		int mx = composite.getSize().x;
		int ymax = composite.getSize().y;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("fullbar"))
        		ymax -= 10; // allow 10px for the bar, even though it only shows 8
        }
        if (ymax < 100)
        	ymax = 100; // if they've shrunk the window down too much, just add scrolling
        int ypos = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("fullbar")) {
        		c.setBounds(0, ypos, mx, 6);
        		ypos += 9;
        	} else if (type.equals("hex")) {
        		c.setBounds(0, ypos, mx, ymax); // remaining space
        		ypos += ymax;
        	}
        }
	}

}
