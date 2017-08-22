package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface BarData {
	int getTotal();
	int getComplete();
	boolean isPassing();

	@Deprecated
	String getId();
	@Deprecated
	String getName();
	@Deprecated
	int getPassed();
	@Deprecated
	int getFailures();
	@Deprecated
	int[] getMarks();
	@Deprecated
	List<String> classesUnderTest();
}
