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
		int xmax = composite.getSize().x;
		int ymax = composite.getSize().y;
		int hexAt = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	System.out.println(type);
        	if (type.equals("accbar")) {
        		ymax -= 10; // allow 10px for the bar, even though it only shows 6, allowing 2 for margin on both sides
        		hexAt += 10;
        	}
        }
        if (ymax < 100)
        	ymax = 100; // if they've shrunk the window down too much, just add scrolling
        int ypos = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("accbar")) {
        		c.setBounds(0, ypos+2, xmax, 6);
        		ypos += 10;
        	} else if (type.equals("hexbg")) {
        		c.setBounds(0, hexAt, xmax, ymax); // remaining space
        	} else if (type.equals("businessbar")) {
        		int a = HexagonPaintListener.figureA(xmax, ymax);
        		c.setBounds((xmax-3*a)/2, hexAt+ymax/2-3, 3*a, 6);
        		System.out.println("Set bounds to " + c.getBounds());
        	} else
        		System.out.println("Don't lay out " + type);
        }
	}

}
