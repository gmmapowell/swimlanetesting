package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface BarData {
	@Deprecated
	String getId();
	@Deprecated
	String getName();
	@Deprecated
	int getComplete();
	@Deprecated
	int getPassed();
	@Deprecated
	int getFailures();
	@Deprecated
	int getTotal();
	@Deprecated
	int[] getMarks();
	@Deprecated
	List<String> classesUnderTest();
}
