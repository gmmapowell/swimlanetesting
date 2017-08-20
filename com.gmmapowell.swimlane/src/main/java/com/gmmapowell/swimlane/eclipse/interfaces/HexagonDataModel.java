package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Deprecated
public interface HexagonDataModel {
	enum Status { NONE, OK, FAILURES, SKIPPED }; 
	public Date getBuildTime();
	public Date getTestCompleteTime();
	public List<BarData> getAcceptanceTests();
	public int getHexCount();
	public Set<String> getErrors();
	public List<HexData> getHexagons();
	public BarData getUtilityBar();
	public Collection<TestResultGroup> getTestResultsFor(String resultsFor);
}
