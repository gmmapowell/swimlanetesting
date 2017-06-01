package com.gmmapowell.swimlane.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;

public class RunAllTestsHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HexagonViewPart hv = HexagonViewPart.get(event);
		if (hv == null)
			return null;
		TestRunner tr = hv.getTestRunner();
		tr.runAll(hv.getDispatcher(), hv.getAccumulator());
		return null;
	}
}