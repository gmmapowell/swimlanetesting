package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

// An object that combines the business logic of being aware of the idea of tests
// with the graphical display of painting them
public class BarControl {
	private final Canvas canvas;

	public BarControl(Composite view, HexagonDataModel model, BarData accModel, String accId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.bar", this);
		canvas.setData("org.eclipse.swtbot.widget.key", accId);
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 6;
		gd.horizontalSpan = model.getHexCount();
		canvas.setLayoutData(gd);
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				int total = accModel.getTotal();
				int compl = accModel.getComplete();
				int[] marks = accModel.getMarks();
				Point size = canvas.getSize();
				int segwidth = size.x/model.getHexCount();
				int markedx = bitcount(marks)*segwidth;
				int barx = 0;
				if (total > 0)
					barx = markedx*compl/total;
				GC gc = new GC(canvas);
				for (int i=0;i<marks.length;i++) {
					int from = size.x*i/model.getHexCount();
					int to = size.x*(i+1)/model.getHexCount();
					if (marks[i] == 1) {
						if (barx > from) {
							Color barColor = view.getDisplay().getSystemColor(getColor(accModel.getStatus()));
							gc.setBackground(barColor);
							gc.fillRectangle(from, 0, Math.min(barx-from, to-from), size.y);
						}
						if (barx < to) {
							int left = Math.max(barx, from);
							Color grey = view.getDisplay().getSystemColor(SWT.COLOR_GRAY);
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
		});
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
