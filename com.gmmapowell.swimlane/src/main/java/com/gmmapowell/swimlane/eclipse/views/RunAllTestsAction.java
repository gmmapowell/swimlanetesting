package com.gmmapowell.swimlane.eclipse.views;

import java.util.Date;

import org.eclipse.jface.action.Action;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class RunAllTestsAction extends Action implements AccumulatorListener {
	private final TestRunner tr;
	private final ModelDispatcher lsnrs;
	private Accumulator model;

	public RunAllTestsAction(TestRunner tr, ModelDispatcher lsnrs) {
		this.tr = tr;
		this.lsnrs = lsnrs;
		setId(HexagonViewPart.RunAllID);
		setText("Run All");
		setToolTipText("Run All Tests");
		setEnabled(false);
	}

	public void run() {
		for (TestGroup g : this.model.getAllTestClasses()) {
			tr.runClass(g.getClassPath(), g.getClasses());
		}
		System.out.println("Run All");
		this.model.testsCompleted(new Date());
		lsnrs.setModel(this.model);
	}

	@Override
	public void setModel(Accumulator model) {
		this.model = model;
		setEnabled(model != null);
	}
}