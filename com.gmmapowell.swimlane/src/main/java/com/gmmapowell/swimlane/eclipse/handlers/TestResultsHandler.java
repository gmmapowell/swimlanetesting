package com.gmmapowell.swimlane.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.gmmapowell.swimlane.eclipse.interfaces.SingleStore;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;

public class TestResultsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SingleStore hv = HexagonViewPart.get(event);
		if (hv == null)
			return null;
		hv.showTestResults(null);
		return true;
	}
}