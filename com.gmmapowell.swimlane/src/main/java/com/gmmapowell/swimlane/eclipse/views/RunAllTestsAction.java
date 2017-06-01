package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.jface.action.Action;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;

public class RunAllTestsAction extends Action implements AccumulatorListener {
	private final TestRunner tr;
	private final ModelDispatcher dispatcher;
	private Accumulator model;

	public RunAllTestsAction(TestRunner tr, ModelDispatcher dispatcher) {
		this.tr = tr;
		this.dispatcher = dispatcher;
		setId(HexagonViewPart.RunAllID);
		setText("Run All");
		setToolTipText("Run All Tests");
		setEnabled(false);
	}

	public void run() {
		tr.runAll(dispatcher, model);
	}

	@Override
	public void setModel(Accumulator model) {
		this.model = model;
		setEnabled(model != null);
	}
}