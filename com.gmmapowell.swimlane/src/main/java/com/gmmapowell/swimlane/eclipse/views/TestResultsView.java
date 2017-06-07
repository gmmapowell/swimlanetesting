package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

public class TestResultsView {
	private final Composite view;
	private final Tree tree;
	private final Table table;

	public TestResultsView(Composite parent) {
		view = new Composite(parent, SWT.NONE);
		view.setLayout(new GridLayout(2, false));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree = new Tree(view, SWT.BORDER);
		tree.setData("org.eclipse.swtbot.widget.key", "hexagons.casesTree");
		table = new Table(view, SWT.BORDER);
		table.setData("org.eclipse.swtbot.widget.key", "hexagons.failure");
	}

	public void resultsFor(String id) {
		// TODO Auto-generated method stub
		
	}

	public Control getTop() {
		return view;
	}

}
