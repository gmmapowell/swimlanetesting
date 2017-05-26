package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class HexagonControl {
	private final Canvas canvas;
//
	public HexagonControl(/* ModelDispatcher dispatcher, */ Composite view, /*HexagonDataModel model, BarData bar, */ String hexId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "hex");
		canvas.setData("com.gmmapowell.swimlane.hex", this);
		canvas.setData("org.eclipse.swtbot.widget.key", hexId);
		canvas.addPaintListener(new HexagonPaintListener(canvas/*,model, bar*/));
//		dispatcher.addBarListener(this);
	}

	public Canvas getBackground() {
		return canvas;
	}
//
//	@Override
//	public void barChanged(BarData bar) {
//		canvas.getDisplay().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				canvas.redraw();
//			}
//		});
//	}
}
