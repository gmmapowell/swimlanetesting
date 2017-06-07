package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface HexagonDataModel {
	enum Status { NONE, OK, FAILURES, SKIPPED }; 
	public Date getBuildTime();
	public Date getTestCompleteTime();
	public List<BarData> getAcceptanceTests();
	public int getHexCount();
	public Set<String> getErrors();
	public List<HexData> getHexagons();
	public BarData getUtilityBar();
	public Set<TestResultGroup> getTestResultsFor(String resultsFor);
}
