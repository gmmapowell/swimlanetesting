package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;

public class SwimlaneLayout extends Layout {

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		return new Point(wHint, hHint);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {

		// I think the drawing happens automatically, so we need to have things in the right "z-axis" order
		// i.e. (I think exclusively) The business bars need to be "in front of" the hexagons
		// But that doesn't say anything about vertically (y-axis)
		
		// Let's try and take a multi-phase approach with a TDA pipeline
		// Phase 1: get everybody to agree on how much space needs to be used for Accs and Ute, and how much is left for hexes
		//   also try and figure out where the acceptance bars should go vertically and hexes horizontally
		//   also which adapters & ports are needed for which hexes and where they should go
		// Then, global action: divvy up the actual space and come up with some concrete numbers
		// Phase 2: go back and tell everyone the space they have to draw themselves in
		
		// Start Phase I by having a helper object exist
		SwimlaneLayoutConstraints constraints = new SwimlaneLayoutConstraints();
		
		System.out.println("Layout called but not implemented with " + composite.getChildren().length);
		for (Control ch : composite.getChildren()) {
			Object ld = ch.getLayoutData();
			System.out.println("  " + ld);
			if (!(ld instanceof SwimlaneLayoutData)) {
				System.out.println("invalid layout data for " + ch);
				continue;
			}
			((SwimlaneLayoutData)ld).constrain(constraints);
		}
		int xmax = composite.getSize().x;
		int ymax = composite.getSize().y;
		for (HexagonBackground bg : constraints.bgs) {
			bg.layout(0, 0, xmax, ymax);
		}
		/*
		// TODO: refactor this into an attribute on generation
		int hexAt = 0;
		int nhexes = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("accbar")) {
        		ymax -= 10; // allow 10px for the bar, even though it only shows 6, allowing 2 for margin on both sides
        		hexAt += 10;
        	} else if (type.equals("utebar")) {
           		ymax -= 10; // allow 10px for the bar, but make it underneath the hexagons
        	} else if (type.equals("hexbg")) {
        		nhexes++;
        	}
        }
        if (ymax < 100)
        	ymax = 100; // if they've shrunk the window down too much, just add scrolling
        int ypos = 0;
        // TODO: refactor these into attributes on generation like we do with ports and adapters
        int hex = 0;
        int hbar = 0;
        for (Control c : composite.getChildren()) {
        	String type = (String) c.getData("com.gmmapowell.swimlane.type");
        	if (type.equals("accbar")) {
        		c.setBounds(0, ypos+2, xmax, 6);
        		ypos += 10;
        	} else if (type.equals("hexbg")) {
        		int midx = xmax*(2*hex+1)/nhexes/2;
        		int a = figureA(xmax/nhexes, ymax);
        		int h = (int)(a*Math.sqrt(3));
				Rectangle r = new Rectangle(midx-2*a, hexAt+ymax/2-h, 4*a, 2*h);
				c.setBounds(r);
        		hex++;
        	} else if (type.equals("businessbar")) {
        		int mid = xmax*hbar/nhexes + xmax/2/nhexes;
        		int a = figureA(xmax/nhexes, ymax);
        		Rectangle r = new Rectangle(mid-3*a/2, hexAt+ymax/2-3, 3*a, 6);
        		c.setBounds(r);
        		hbar++;
        	} else if (type.equals("port")) {
        		int hexn = (int)c.getData("com.gmmapowell.swimlane.hexagon")-1;
        		PortLocation pl = (PortLocation)c.getData("com.gmmapowell.swimlane.location");
        		int midx = xmax*hexn/nhexes + xmax/2/nhexes;
        		int midy = hexAt+ymax/2;
        		int a = figureA(xmax/nhexes, ymax);
        		int y1 = midy + pl.y((int) (Math.sqrt(3)*a));
        		int y2 = midy + pl.y((int) (Math.sqrt(3)*a/2));
        		int x1 = midx + pl.x(3*a/2);
        		int x2 = x1 + pl.x(a/2+10);
        		Rectangle r = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1-x2), Math.abs(y1-y2));
        		c.setBounds(r);
        	} else if (type.equals("adapterbar")) {
        		int hexn = (int)c.getData("com.gmmapowell.swimlane.hexagon")-1;
        		int midx = xmax*hexn/nhexes + xmax/2/nhexes;
        		int midy = hexAt+ymax/2;
        		int a = figureA(xmax/nhexes, ymax);
        		PortLocation pl = (PortLocation)c.getData("com.gmmapowell.swimlane.location");
        		int an = (int)c.getData("com.gmmapowell.swimlane.adapterPos");
        		int atot = (int)c.getData("com.gmmapowell.swimlane.adapterTotal");
        		// Each bar has 10 allocated, but 6 used, so total vertical space is 10*total-4
        		int totY = 10*atot-4;
        		// find a point halfwqy up the port (see y1 and y2 above)
        		int median = midy + pl.y((int) (Math.sqrt(3)*3*a/4));
        		// first is "above" median line, so take half this off (this is never reflected based on location)
        		int first = median - totY/2;
        		// and now add (an-1)*10 back on for the ones above (an is offset 1, hence -1)
        		int y1 = first + (an-1)*10;
        		int x1 = midx + pl.x(2*a+10);
        		int x2 = x1 + pl.x(a/2);
        		Rectangle r = new Rectangle(Math.min(x1, x2), y1, Math.abs(x1-x2), 6);
        		c.setBounds(r);
        	} else if (type.equals("utebar")) {
        		c.setBounds(0, hexAt+ymax+2, xmax, 6);
        	} else
        		System.out.println("Don't lay out " + type);
        }
        */
	}

	public static int figureA(int wx, int hy) {
		int ya = hy*6/25;
		int xa = wx/5;
		return Math.min(xa, ya);
	}

}
