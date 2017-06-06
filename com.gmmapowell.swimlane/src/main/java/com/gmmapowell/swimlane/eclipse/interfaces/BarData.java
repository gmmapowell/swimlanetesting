package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public interface BarData {
	String getId();
	String getName();
	Status getStatus();
	int getComplete();
	int getPassed();
	int getFailures();
	int getTotal();
	int[] getMarks();
	List<String> classesUnderTest();
}
