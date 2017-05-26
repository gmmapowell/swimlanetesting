package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public final class BarPaintListener implements PaintListener {
	private final BarData barModel;
	private final Canvas canvas;

	public BarPaintListener(Canvas canvas, BarData accModel) {
		this.canvas = canvas;
		this.barModel = accModel;
	}

	@Override
	public void paintControl(PaintEvent e) {
		int total = barModel.getTotal();
		int compl = barModel.getComplete();
		int[] marks = barModel.getMarks();
		Point size = canvas.getSize();
		System.out.println("Painting bar of size " + size);
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
					Color barColor = canvas.getDisplay().getSystemColor(getColor(barModel.getStatus()));
					gc.setBackground(barColor);
					gc.fillRectangle(from, 0, Math.min(barx-from, to-from), size.y);
				}
				if (barx < to) {
					int left = Math.max(barx, from);
					Color grey = canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY);
					gc.setBackground(grey);
					gc.fillRectangle(left, 0, Math.min(markedx-left, to-from), size.y);
				}
			} else {
				barx += segwidth;
				markedx += segwidth;
			}
		}
		gc.dispose();
	}

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
}