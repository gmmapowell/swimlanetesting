package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ShowErrorsPane;

// An object that combines the business logic of being aware of the idea of tests
// with the graphical display of painting them
public class BarControl implements BarDataListener, PaintListener {
	private final static Logger logger = LoggerFactory.getLogger("BarControl");
//	private final String type;
	private final Canvas canvas;
//	private final BarPaintListener bpl;
	private BarData barData;
	private String name;

	public BarControl(Composite view, String name, ShowErrorsPane showErrors) {
		this.name = name;
//		this.type = type;
		canvas = new Canvas(view, SWT.NONE);
//		canvas.setData("com.gmmapowell.swimlane.type", type);
//		canvas.setData("com.gmmapowell.swimlane.bar", this);
		canvas.setData("org.eclipse.swtbot.widget.key", "swimlane.bar." + name);
		canvas.setLayoutData(this);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (barData != null)
					showErrors.showFor(barData);
			}
		});
//		bpl = new BarPaintListener(canvas, bar);
		canvas.addPaintListener(this);
		canvas.moveAbove(null); // move to top of drawing order
//		barChanged(bar);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void barChanged(BarData updated) {
		this.barData = updated;
		canvas.getDisplay().asyncExec(() -> updateBarInDisplayThread());
	}

	private void updateBarInDisplayThread() {
		boolean vis = barData != null && barData.getTotal() > 0;
		canvas.setVisible(vis);
		logger.info("Setting bar " + name + " visibility to " + vis);
		if (vis) {
			String tooltip = barData.getTooltip(name);
			logger.info("Setting bar " + name + " tooltip to " + tooltip);
			canvas.setToolTipText(tooltip);
		}
		canvas.redraw();
	}
	
	// Thiese are public because we currently can't test the actual drawing, so we test these instead
	public int total() {
		return canvas.getSize().x;
	}
	
	public int progress() {
		return canvas.getSize().x*barData.getComplete()/barData.getTotal();
	}
	
	public int color() {
		if (barData.isPassing())
			return SWT.COLOR_GREEN;
		else
			return SWT.COLOR_RED;
	}
	
	@Override
	public void paintControl(PaintEvent e) {
		if (barData == null)
			return;
		// TODO: that light/dark 3d framing effect
		GC gc = new GC(canvas);
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, total(), canvas.getSize().y);
		if (total() > 0) {
			gc.setBackground(canvas.getDisplay().getSystemColor(color()));
			gc.fillRectangle(0, 0, progress(), canvas.getSize().y);
		}
		/*
		int total = 0;
		int compl = 0;
		int[] marks = new int[1];
		Status stat = Status.NONE;
		if (barModel != null) {
			total = barModel.getTotal();
			compl = barModel.getComplete();
			marks = barModel.getMarks();
			stat = barModel.getStatus();
		}
		Point size = canvas.getSize();
		int hexCount = marks.length;
		int segwidth = size.x/hexCount;
		int markedx = bitcount(marks)*segwidth;
		int barx = 0;
		if (total > 0)
			barx = markedx*compl/total;
		GC gc = new GC(canvas);
		for (int i=0;i<marks.length;i++) {
			int from = size.x*i/hexCount;
			int to = size.x*(i+1)/hexCount;
			if (marks[i] == 1) {
				if (barx > from) {
					Color barColor = canvas.getDisplay().getSystemColor(getColor(stat));
					gc.setBackground(barColor);
					int cw = Math.min(barx-from, to-from);
					gc.fillRectangle(from, 0, cw, size.y);
				}
				if (barx < to) {
					int left = Math.max(barx, from);
					Color grey = canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY);
					gc.setBackground(grey);
					int gw = Math.min(markedx-left, to-from);
					gc.fillRectangle(left, 0, gw, size.y);
				}
			} else {
				barx += segwidth;
				markedx += segwidth;
			}
		}
		gc.dispose();
		*/
	}

	/*
	private int bitcount(int[] marks) {
		int sum = 0;
		for (int i=0;i<marks.length;i++)
			sum+=marks[i];
		return sum;
	}

	private int getColor(Status status) {
		switch (status) {
		case NONE:
			return SWT.COLOR_GRAY;
		case OK:
			return SWT.COLOR_GREEN;
		case FAILURES:
			return SWT.COLOR_RED;
		case SKIPPED:
			return SWT.COLOR_YELLOW;
		}
		throw new RuntimeException("Cannot handle status " + status);
	}

	public String getBarName() {
		if (barModel == null)
			return null;
		else
			return barModel.getName();
	}
	*/
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
	 */
}
