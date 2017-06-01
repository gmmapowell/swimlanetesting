package com.gmmapowell.swimlane.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;

public class RunAllTestsHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart me = HandlerUtil.getActivePart(event);
		if (me == null)
			return null;
		HexagonViewPart hv = me.getAdapter(HexagonViewPart.class);
		if (hv == null)
			return null;
		System.out.println("hexview = " + hv);
		TestRunner tr = hv.getTestRunner();
		tr.runAll(hv.getDispatcher(), hv.getAccumulator());
		return null;
	}
}