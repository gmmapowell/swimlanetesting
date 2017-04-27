package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.jface.action.Action;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;

public class RunAllTestsAction extends Action implements HexagonModelListener {
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