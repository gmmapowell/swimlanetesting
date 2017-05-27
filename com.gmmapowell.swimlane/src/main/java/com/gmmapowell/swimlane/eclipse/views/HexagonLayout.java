package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class HexagonLayout extends Layout {

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		return new Point(wHint, hHint);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		int xmax = composite.getSize().x;
		int ymax = composite.getSize().y;
		int hexAt = 0;
		int nhexes = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("accbar")) {
        		ymax -= 10; // allow 10px for the bar, even though it only shows 6, allowing 2 for margin on both sides
        		hexAt += 10;
        	} else if (type.equals("hexbg")) {
        		nhexes++;
        	}
        }
        if (ymax < 100)
        	ymax = 100; // if they've shrunk the window down too much, just add scrolling
        int ypos = 0;
        int hex = 0;
        int hbar = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("accbar")) {
        		c.setBounds(0, ypos+2, xmax, 6);
        		ypos += 10;
        	} else if (type.equals("hexbg")) {
        		int left = xmax*hex/nhexes;
				int right = xmax*(hex+1)/nhexes;
				Rectangle r = new Rectangle(left, hexAt, right-left, ymax);
				c.setBounds(r);
        		hex++;
        	} else if (type.equals("businessbar")) {
        		int mid = xmax*hbar/nhexes + xmax/2/nhexes;
        		int a = HexagonPaintListener.figureA(xmax/nhexes, ymax);
        		Rectangle r = new Rectangle(mid-3*a/2, hexAt+ymax/2-3, 3*a, 6);
        		c.setBounds(r);
        		hbar++;
        	} else if (type.equals("port")) {
        		int hexn = (int)c.getData("com.gmmapowell.swimlane.hexagon")-1;
        		PortLocation pl = (PortLocation)c.getData("com.gmmapowell.swimlane.location");
        		int midx = xmax*hexn/nhexes + xmax/2/nhexes;
        		int midy = hexAt+ymax/2;
        		int a = HexagonPaintListener.figureA(xmax/nhexes, ymax);
        		int y1 = midy + pl.y((int) (Math.sqrt(3)*a));
        		int y2 = midy + pl.y((int) (Math.sqrt(3)*a/2));
        		int x1 = midx + pl.x(3*a/2);
        		int x2 = x1 + pl.x(a/2+10);
        		Rectangle r = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1-x2), Math.abs(y1-y2));
        		c.setBounds(r);
        	} else
        		System.out.println("Don't lay out " + type);
        }
	}

}
