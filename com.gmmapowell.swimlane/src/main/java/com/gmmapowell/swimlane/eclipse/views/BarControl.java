package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

// An object that combines the business logic of being aware of the idea of tests
// with the graphical display of painting them
public class BarControl implements BarDataListener {
	private final String type;
	private final Canvas canvas;
	private final BarPaintListener bpl;

	public BarControl(Composite view, BarData bar, String type, String barId) {
		this.type = type;
		canvas = new Canvas(view, SWT.NONE);
//		canvas.setData("com.gmmapowell.swimlane.type", type);
//		canvas.setData("com.gmmapowell.swimlane.bar", this);
//		canvas.setData("org.eclipse.swtbot.widget.key", barId);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO: fix this
//				dispatcher.barClicked(barId.substring("hexagons.".length()));
			}
		});
		bpl = new BarPaintListener(canvas, bar);
//		canvas.addPaintListener(bpl);
//		barChanged(bar);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void clearGroup(GroupOfTests grp) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public void testCompleted(TestInfo ti) {
		throw new RuntimeException("not implemented");
	}
	
	/*
	@Override
	public void barChanged(BarData bar) {
		BarData old;
		if ((old = bpl.updateBar(bar)) != null) {
//			dispatcher.removeBarListener(old, this);
//			dispatcher.addBarListener(bar, this);
		}
		canvas.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean vis = bar != null && bar.getTotal() > 0;
				canvas.setVisible(vis);
				if (vis) {
					String tn = typeName();
					StringBuilder sb = new StringBuilder();
					if (tn != null) {
						sb.append(tn);
						sb.append(" - ");
					}
					sb.append("1 group; ");
					sb.append(bar.getPassed());
					sb.append(" passed");
					int failed = bar.getFailures();
					if (failed > 0) {
						sb.append(", ");
						sb.append(failed);
						sb.append(" failure");
						if (failed != 1)
							sb.append("s");
					}
					String tooltip = sb.toString();
					canvas.setToolTipText(tooltip);
				}
				canvas.redraw();
			}

		});
	}

	private String typeName() {
		switch (type) {
		case "accbar":
			return "Acceptance";
		case "utebar":
			return "Utilities";
		case "businessbar":
		case "adapterbar":
		{
			String bn = bpl.getBarName();
			if (bn == null)
				return null;
			return bn.substring(bn.lastIndexOf('.')+1);
		}
		default:
			return type;
		}
	}
	 */
}
