package com.gmmapowell.swimlane.eclipse.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;

public class RunAllTestsAction extends AbstractHandler implements AccumulatorListener {
//	private final TestRunner tr;
//	private final ModelDispatcher dispatcher;
//	private Accumulator model;

	public RunAllTestsAction() {
	}
	
//	public RunAllTestsAction(TestRunner tr, ModelDispatcher dispatcher) {
//		this.tr = tr;
//		this.dispatcher = dispatcher;
//		setEnabled(false);
//	}
//
//	public void run() {
//		tr.runAll(dispatcher, model);
//	}

	@Override
	public void setModel(Accumulator model) {
//		this.model = model;
		setEnabled(model != null);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Run All Tests");
		// TODO Auto-generated method stub
		return null;
	}
}