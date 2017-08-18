package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

public interface AnalysisAccumulator {
	void startAnalysis(Date startTime);
	void clean(GroupOfTests grp);
	void haveTestClass(GroupOfTests grp, String clzName, TestRole role);
	void analysisComplete(Date completeTime);
}
