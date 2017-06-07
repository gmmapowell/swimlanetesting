package com.gmmapowell.swimlane.eclipse.views;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultGroup;

public class TestResultsView implements HexagonModelListener {
	private final Composite view;
	private final Tree tree;
	private final Table table;
	private String resultsFor;

	public TestResultsView(Composite parent, ModelDispatcher dispatcher) {
		view = new Composite(parent, SWT.NONE);
		view.setLayout(new GridLayout(2, false));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree = new Tree(view, SWT.BORDER);
		tree.setData("org.eclipse.swtbot.widget.key", "hexagons.casesTree");
		table = new Table(view, SWT.BORDER);
		table.setData("org.eclipse.swtbot.widget.key", "hexagons.failure");
		
		dispatcher.addHexagonModelListener(this);
	}

	public void resultsFor(String id) {
		this.resultsFor = id;
	}

	public Control getTop() {
		return view;
	}

	@Override
	public void setModel(HexagonDataModel model) {
		view.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.removeAll();
				table.removeAll();
				Set<TestResultGroup> groups = model.getTestResultsFor(resultsFor);
				for (TestResultGroup grp : groups) {
					System.out.println("have a group");
					TreeItem item = new TreeItem(tree, SWT.NONE);
					item.setText("hello");
				}
			}
		});
	}

}
