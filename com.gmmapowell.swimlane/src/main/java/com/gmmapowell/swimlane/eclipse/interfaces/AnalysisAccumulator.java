package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

import com.gmmapowell.swimlane.eclipse.models.GroupOfTests;

public interface AnalysisAccumulator {
	void startAnalysis(Date startTime);
	void haveTestClass(GroupOfTests grp, String clzName, TestRole role);
	void error(String message);
	void analysisComplete(Date completeTime);

}
