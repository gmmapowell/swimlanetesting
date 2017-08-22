package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class HexagonBackground extends LayoutPlacer implements PaintListener {
//	private final BarControl bar;
//	private final List<PortControl> ports = new ArrayList<PortControl>();

	public HexagonBackground(Composite view, int hex /*, String hexId, BarData bar, Collection<PortData> ports */) {
		super(new Canvas(view, SWT.NONE));
//		canvas.setData("com.gmmapowell.swimlane.type", "hexbg");
//		canvas.setData("com.gmmapowell.swimlane.hex", this);
		canvas.setData("org.eclipse.swtbot.widget.key", "swimlane.hexbg." + hex);
		canvas.setLayoutData(this);
		canvas.addPaintListener(this);
		canvas.moveBelow(null); // move to bottom of drawing order
//		this.bar = new BarControl(view /*, bar, "businessbar", hexId+".bar" */);
//		for (PortData p : ports) {
//			// should we keep track of these somehow?
//			this.ports.add(new PortControl(view, hexId+".port."+p.getLocation().toString(), hex, p));
//			int j=1;
//			int total = p.getAdapters().size();
//			for (BarData ad : p.getAdapters()) {
//				BarControl bc = new BarControl(view, ad, "adapterbar", hexId +".adapter."+p.getLocation().toString()+"." + j);
//				bc.getCanvas().setData("com.gmmapowell.swimlane.hexagon", hex);
//				bc.getCanvas().setData("com.gmmapowell.swimlane.location", p.getLocation());
//				bc.getCanvas().setData("com.gmmapowell.swimlane.adapterPos", j);
//				bc.getCanvas().setData("com.gmmapowell.swimlane.adapterTotal", total);
//				j++;
//			}
//		}
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.background(this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		int mx = canvas.getSize().x/2;
		int my = canvas.getSize().y/2;
		int a = mx/2; // because we have sized to 4a x R(3)*2
		int h = (int) (Math.sqrt(3)*a);
		int ty = my-h, by = my+h;
		
		GC gc = new GC(canvas);
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.fillPolygon(new int[] { mx-2*a, my, mx-a, ty, mx+a, ty, mx+2*a, my, mx+a, by, mx-a, by });
		gc.dispose();
	}
	/*
	public Canvas getBackground() {
		return canvas;
	}

	public Canvas getBar() {
		return bar.getCanvas();
	}

	public List<PortControl> getPorts() {
		return ports;
	}

	public void barChanged(BarData hcb) {
		bar.getCanvas().setVisible(hcb != null && hcb.getTotal() > 0);
		bar.barChanged(hcb);
	}
	*/
}
