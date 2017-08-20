package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

public interface AnalysisAccumulator {
	void clean(GroupOfTests grp);
	void haveTestClass(GroupOfTests grp, String clzName, TestRole role, List<String> tests);
	void analysisComplete(Date completeTime);
}
