package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.jface.action.Action;

public class RunAllTestsAction extends Action {
	@Override
	public String getId() {
		return HexagonViewPart.RunAllID;
	}

	public void run() {
		System.out.println("Run All");
	}

	@Override
	public String getText() {
		return "Run All";
	}

	@Override
	public String getToolTipText() {
		return "Run All Tests";
	}
}