package com.gmmapowell.swimlane.eclipse.interfaces;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public interface BarData {
	String getId();
	Status getStatus();
	int getComplete();
	int getTotal();
	int[] getMarks();
}
