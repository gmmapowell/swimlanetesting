package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

public interface HexagonDataModel {
	enum Status { NONE, OK, FAILURES, SKIPPED }; 
	public Date getBuildTime();
	public List<BarData> getAcceptanceTests();
	public int getHexCount();
	public List<String> getErrors();
}
