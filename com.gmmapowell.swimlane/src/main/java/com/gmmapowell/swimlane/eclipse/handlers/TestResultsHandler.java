package com.gmmapowell.swimlane.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.gmmapowell.swimlane.eclipse.interfaces.CommandDispatcher;
import com.gmmapowell.swimlane.eclipse.views.SwimlaneViewPart;

public class TestResultsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CommandDispatcher hv = SwimlaneViewPart.get(event);
		if (hv == null)
			return null;
		hv.showTestResults(null);
		return true;
	}
}