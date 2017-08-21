package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

public class TestResultsView {
	private final Composite view;
	private final Tree tree;
	private final Table table;
	private String resultsFor;

	public TestResultsView(Composite parent) {
		view = new Composite(parent, SWT.NONE);
//		view.setLayout(new GridLayout(2, false));
//		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree = new Tree(view, SWT.BORDER);
//		tree.setData("org.eclipse.swtbot.widget.key", "hexagons.casesTree");
//		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table = new Table(view, SWT.BORDER);
//		table.setData("org.eclipse.swtbot.widget.key", "hexagons.failure");
//		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void resultsFor(String id) {
		throw new RuntimeException("not implemented");
//		this.resultsFor = id;
//		updateDisplay();
	}

	public Control getTop() {
		return view;
	}

	protected void updateDisplay() {
		throw new RuntimeException("not implemented");
		/*
		view.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.removeAll();
				table.removeAll();
				if (resultsFor == null) {
					System.out.println("resultsFor not set; displaying nothing");
					return;
				}
				Collection<TestResultGroup> groups = model.getTestResultsFor(resultsFor);
				for (TestResultGroup grp : groups) {
					TreeItem gi = new TreeItem(tree, SWT.NONE);
					gi.setText(grp.name());
					for (TestResultClass clz : grp.testClasses()) {
						TreeItem ci = new TreeItem(gi, SWT.NONE);
						ci.setText(clz.className());
						for (TestInfo test : clz.tests()) {
							TreeItem ti = new TreeItem(ci, SWT.NONE);
							ti.setText(test.testName());
						}
					}
				}
			}
		});
		 */
	}
}
