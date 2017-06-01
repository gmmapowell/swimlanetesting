package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ErrorView {
	private final Composite view;
	private final Table table;

	public ErrorView(Composite parent) {
		view = new Composite(parent, SWT.NONE);
		view.setLayout(new GridLayout(1, false));
		view.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table = new Table(view, SWT.BORDER);
		table.setData("org.eclipse.swtbot.widget.key", "hexagons.errors");
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		
		TableColumn tc = new TableColumn(table, SWT.NULL);
		tc.setText("Hello");
		
		TableItem ti = new TableItem(table, SWT.NULL);
		ti.setText("Yo!");
		ti.setText(0, "There");
	}

	public Control getTop() {
		return view;
	}

}
