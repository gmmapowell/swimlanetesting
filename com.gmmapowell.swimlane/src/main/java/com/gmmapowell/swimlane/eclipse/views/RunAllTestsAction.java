package com.gmmapowell.swimlane.eclipse.views;

import java.util.Date;

import org.eclipse.jface.action.Action;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class RunAllTestsAction extends Action implements AccumulatorListener {
	private final ModelDispatcher lsnrs;
	private Accumulator model;

	public RunAllTestsAction(ModelDispatcher lsnrs) {
		this.lsnrs = lsnrs;
		setId(HexagonViewPart.RunAllID);
		setText("Run All");
		setToolTipText("Run All Tests");
		setEnabled(false);
	}

	public void run() {
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