package com.gmmapowell.swimlane.eclipse.views;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class HexagonControl {
	private final Canvas canvas;
	private final BarControl bar;

	public HexagonControl(ModelDispatcher dispatcher, Composite view, String hexId, BarData bar, Collection<PortData> ports) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "hexbg");
		canvas.setData("com.gmmapowell.swimlane.hex", this);
		canvas.setData("org.eclipse.swtbot.widget.key", hexId+".bg");
		canvas.addPaintListener(new HexagonPaintListener(canvas/*,model, bar*/));
		this.bar = new BarControl(dispatcher, view, bar, "businessbar", hexId+".bar");
		for (PortData p : ports) {
			// should we keep track of these somehow?
			new PortControl(view, hexId+".port."+p.getLocation().toString());
			int j=1;
			for (AdapterData ad : p.getAdapters()) {
				new AdapterControl(view, hexId +".adapter."+p.getLocation().toString()+"." + j);
				j++;
			}
		}
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
