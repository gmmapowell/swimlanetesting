package com.gmmapowell.swimlane.eclipse.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class HexagonControl {
	private final Canvas canvas;
	private final BarControl bar;
	private final List<PortControl> ports = new ArrayList<PortControl>();

	public HexagonControl(ModelDispatcher dispatcher, Composite view, int hex, String hexId, BarData bar, Collection<PortData> ports) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "hexbg");
		canvas.setData("com.gmmapowell.swimlane.hex", this);
		canvas.setData("org.eclipse.swtbot.widget.key", hexId+".bg");
		canvas.addPaintListener(new HexagonPaintListener(canvas/*,model, bar*/));
		this.bar = new BarControl(dispatcher, view, bar, "businessbar", hexId+".bar");
		for (PortData p : ports) {
			// should we keep track of these somehow?
			this.ports.add(new PortControl(view, hexId+".port."+p.getLocation().toString(), hex, p.getLocation()));
			int j=1;
			int total = p.getAdapters().size();
			for (BarData ad : p.getAdapters()) {
				BarControl bc = new BarControl(dispatcher, view, ad, "adapterbar", hexId +".adapter."+p.getLocation().toString()+"." + j);
				bc.getCanvas().setData("com.gmmapowell.swimlane.hexagon", hex);
				bc.getCanvas().setData("com.gmmapowell.swimlane.location", p.getLocation());
				bc.getCanvas().setData("com.gmmapowell.swimlane.adapterPos", j);
				bc.getCanvas().setData("com.gmmapowell.swimlane.adapterTotal", total);
				j++;
			}
		}
	}

	public Canvas getBackground() {
		return canvas;
	}

	public Canvas getBar() {
		return bar.getCanvas();
	}

	public List<PortControl> getPorts() {
		return ports;
	}
}
