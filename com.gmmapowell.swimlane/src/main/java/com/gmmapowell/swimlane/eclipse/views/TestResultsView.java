package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupTraverser;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestResultsView implements GroupTraverser {
	private final Composite view;
	private final Tree tree;
	private final Table table;
	private BarData resultsFor;
	private TreeItem currentGroup;
	private TreeItem currentTest;

	public TestResultsView(Composite parent) {
		view = new Composite(parent, SWT.NONE);
		view.setLayout(new GridLayout(2, false));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree = new Tree(view, SWT.BORDER);
		tree.setData("org.eclipse.swtbot.widget.key", "swimlane.casesTree");
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table = new Table(view, SWT.BORDER);
		table.setData("org.eclipse.swtbot.widget.key", "swimlane.failure");
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void resultsFor(BarData bar) {
		this.resultsFor = bar;
		view.getDisplay().asyncExec(() -> updateDisplay());
	}

	public Control getTop() {
		return view;
	}

	protected void updateDisplay() {
		tree.removeAll();
		table.removeAll();
		if (resultsFor == null) {
			System.out.println("resultsFor not set; displaying nothing");
			return;
		}
		resultsFor.traverseTree(this);
	}

	@Override
	public void group(GroupOfTests grp) {
		currentGroup = new TreeItem(tree, SWT.NONE);
		currentGroup.setText(grp.groupName());
	}

	@Override
	public void testClass(String testClassName) {
		currentTest = new TreeItem(currentGroup, SWT.NONE);
		currentTest.setText(testClassName);
	}

	@Override
	public void testCase(TestInfo tci) {
		TreeItem item = new TreeItem(currentTest, SWT.NONE);
		item.setText(tci.testName());
	}
}
