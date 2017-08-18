package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;

public class ErrorView implements ErrorMessageListener {
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
		tc.setText("Sev");
		
		TableColumn tc2 = new TableColumn(table, SWT.NULL);
		tc2.setText("Message");
		
		table.getColumn(0).pack();
		table.getColumn(1).pack();
	}

	public Control getTop() {
		return view;
	}

	
	@Override
	public void clear(/* TODO: this should be more selective, i.e. project/testgroup level */) {
		view.getDisplay().asyncExec(new Runnable() {
			public void run() {
				table.removeAll();
			}
		});
	}

	@Override
	public void error(String message) {
		view.getDisplay().asyncExec(new Runnable() {
			public void run() {
				TableItem ti = new TableItem(table, SWT.NULL);
//			ti.setText(0, "There");
				ti.setText(1, message);
			}
		});
	}

}
