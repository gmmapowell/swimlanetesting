package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

// An object that combines the business logic of being aware of the idea of tests
// with the graphical display of painting them
public class BarControl {
	private final Canvas canvas;

	public BarControl(Composite view, HexagonDataModel model, BarData accModel, String accId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.bar", this);
		canvas.setData("org.eclipse.swtbot.widget.key", accId);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 6;
		gd.horizontalSpan = model.getHexCount();
		canvas.setLayoutData(gd);
		canvas.addPaintListener(new BarPaintListener(canvas, model, accModel));
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
