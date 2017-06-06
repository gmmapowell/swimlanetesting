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
	private final ModelDispatcher dispatcher;
	private final String type;
	private final Canvas canvas;
	private final BarPaintListener bpl;

	public BarControl(ModelDispatcher dispatcher, Composite view, BarData bar, String type, String barId) {
		this.dispatcher = dispatcher;
		this.type = type;
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", type);
		canvas.setData("com.gmmapowell.swimlane.bar", this);
		canvas.setData("org.eclipse.swtbot.widget.key", barId);
		bpl = new BarPaintListener(canvas, bar);
		canvas.addPaintListener(bpl);
		dispatcher.addBarListener(bar, this);
		barChanged(bar);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void barChanged(BarData bar) {
		BarData old;
		if ((old = bpl.updateBar(bar)) != null) {
			dispatcher.addBarListener(bar, this);
			dispatcher.removeBarListener(old, this);
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
					canvas.setToolTipText(sb.toString());
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
}
