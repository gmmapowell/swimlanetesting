package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;

public final class BarPaintListener implements PaintListener {
	private BarData barModel;
	private final Canvas canvas;

	public BarPaintListener(Canvas canvas, BarData bar) {
		this.canvas = canvas;
		this.barModel = bar;
	}
	
	public BarData updateBar(BarData upd) {
		if (this.barModel == upd)
			return null;
		BarData tmp = this.barModel;
		this.barModel = upd;
		return tmp;
	}

	@Override
	public void paintControl(PaintEvent e) {
		throw new RuntimeException("not implemented");
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
}