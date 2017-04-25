package com.gmmapowell.swimlane.eclipse.models;

import java.util.Date;
import java.util.List;

public interface HexagonDataModel {
	enum Status { NONE, OK, FAILURES, SKIPPED }; 
	public Date getBuildTime();
	public List<BarData> getAcceptanceTests();
	public int getHexCount();
}
