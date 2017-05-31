package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

// An object that combines the business logic of being aware of the idea of tests
// with the graphical display of painting them
public class BarControl implements BarDataListener {
	private final Canvas canvas;

	public BarControl(ModelDispatcher dispatcher, Composite view, BarData bar, String type, String barId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", type);
		canvas.setData("com.gmmapowell.swimlane.bar", this);
		canvas.setData("org.eclipse.swtbot.widget.key", barId);
		canvas.addPaintListener(new BarPaintListener(canvas, bar));
		dispatcher.addBarListener(bar, this);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void barChanged(BarData bar) {
		canvas.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				canvas.setVisible(bar != null && bar.getTotal() > 0);
				canvas.redraw();
			}
		});
	}
}
