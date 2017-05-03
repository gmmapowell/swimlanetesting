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
        int ypos = 0;
        for (Control c : composite.getChildren()) {
        	c.setBounds(0, ypos, mx, 6);
        	ypos += 9;
        }
	}

}
