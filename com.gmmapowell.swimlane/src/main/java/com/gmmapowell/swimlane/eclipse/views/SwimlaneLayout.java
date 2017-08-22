package com.gmmapowell.swimlane.eclipse.views;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Point;
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
		
		for (Control ch : composite.getChildren()) {
			Object ld = ch.getLayoutData();
			if (!(ld instanceof SwimlaneLayoutData)) {
				System.out.println("invalid layout data for " + ch);
				continue;
			}
			((SwimlaneLayoutData)ld).constrain(constraints);
		}
		int xmax = composite.getSize().x;
		int ymax = composite.getSize().y;
		if (constraints.utility != null)
			ymax -= 10;
		int hexTop = 0;
		if (!constraints.bgs.isEmpty()) {
			int nhexes = constraints.bgs.size();
			int a = figureA(xmax/nhexes, ymax - 10*constraints.acceptances.size());

			// TODO: I think we want to limit the total width in use if there is only one hex and the view is "widescreen"
			// For now, use all of it
			// The TreeMap makes sure these are in the right order
			for (AcceptanceBarLayout bar : constraints.acceptances.values()) {
				bar.layout(0, hexTop+2, xmax, 6);
				hexTop += 10;
				ymax-=10;
			}
			
			int whichHex = 0;
			int h = (int)(a*Math.sqrt(3));
			for (HexagonBackground bg : constraints.bgs) {
        			int midx = xmax*(2*whichHex+1)/nhexes/2;
        			int midy = hexTop+ymax/2;
    				int width = 4*a;
				bg.layout(midx-2*a, midy-h, width, 2*h);
				SwimlaneLayoutData bar = constraints.businessBars.get(bg);
				if (bar != null)
					bar.layout(midx-width*3/10, midy-3, width*3/5, 6);
				Map<PortLocation, PortControl> ports = constraints.ports.get(whichHex);
				if (ports != null) {
					for (Entry<PortLocation, PortControl> e : ports.entrySet()) {
						PortLocation pl = e.getKey();
						int x1 = midx + pl.x(3*a/2);
						int x2 = x1 + pl.x(a/2+10);
			        		int y1 = midy + pl.y((int) (Math.sqrt(3)*a));
			        		int y2 = midy + pl.y((int) (Math.sqrt(3)*a/2));
			        		e.getValue().layout(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1-x2), Math.abs(y1-y2));
					}
				}
				Map<PortLocation, List<SwimlaneLayoutData>> adapters = constraints.adapters.get(whichHex);
				if (adapters != null) {
					for (Entry<PortLocation, List<SwimlaneLayoutData>> e : adapters.entrySet()) {
						PortLocation pl = e.getKey();
						List<SwimlaneLayoutData> list = e.getValue();
						int atot = list.size();
						for (int an=0;an<atot;an++) {
							// Each bar has 10 allocated, but 6 used, so total vertical space is 10*total-4
				        		int totY = 10*atot-4;
				        		// find a point halfwqy up the port (see y1 and y2 above)
				        		int median = midy + pl.y((int) (Math.sqrt(3)*3*a/4));
				        		// first is "above" median line, so take half this off (this is never reflected based on location)
				        		int first = median - totY/2;
				        		// and now add an*10 back on for the ones above
				        		int y1 = first + an*10;
				        		int x1 = midx + pl.x(2*a+10);
				        		int x2 = x1 + pl.x(a/2);
				        		list.get(an).layout(Math.min(x1, x2), y1, Math.abs(x1-x2), 6);
						}
					}
				}
				whichHex++;
			}
			hexTop += ymax;
		}
		if (constraints.utility != null)
			constraints.utility.layout(0, hexTop+2, xmax, 6);
	}

	public static int figureA(int wx, int hy) {
		int ya = hy*6/25;
		int xa = (wx-20)/5;
		return Math.min(xa, ya);
	}

}
