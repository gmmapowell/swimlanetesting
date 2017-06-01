package com.gmmapowell.swimlane.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;

public class ShowHexesHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HexagonViewPart hv = HexagonViewPart.get(event);
		if (hv == null)
			return null;
		hv.showHexPane();
		return true;
	}
}