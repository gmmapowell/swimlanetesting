package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class ErrorView implements HexagonModelListener {
	private final Composite view;
	private final Table table;

	public ErrorView(Composite parent, ModelDispatcher dispatcher) {
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
		
		dispatcher.addHexagonModelListener(this);
	}

	public Control getTop() {
		return view;
	}

	@Override
	public void setModel(HexagonDataModel model) {
		table.clearAll();
		for (String s : model.getErrors()) {
			TableItem ti = new TableItem(table, SWT.NULL);
//			ti.setText(0, "There");
			ti.setText(1, s);
		}
	}

}
