package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class HexagonControl {
	private final Canvas canvas;
	private final BarControl bar;

	public HexagonControl(ModelDispatcher dispatcher, Composite view, BarData bar, String hexId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "hexbg");
		canvas.setData("com.gmmapowell.swimlane.hex", this);
		canvas.setData("org.eclipse.swtbot.widget.key", hexId+".bg");
		canvas.addPaintListener(new HexagonPaintListener(canvas/*,model, bar*/));
		this.bar = new BarControl(dispatcher, view, bar, "businessbar", hexId+".bar");
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

	public Canvas getBar() {
		return bar.getCanvas();
	}
}
