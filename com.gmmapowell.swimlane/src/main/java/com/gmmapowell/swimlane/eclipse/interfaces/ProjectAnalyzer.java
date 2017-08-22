package com.gmmapowell.swimlane.eclipse.interfaces;

import java.net.URLClassLoader;
import java.util.Date;

import com.gmmapowell.swimlane.eclipse.models.GroupOfTests;

public interface ProjectAnalyzer {
	void startAnalysis(Date startTime);
	void consider(GroupOfTests grp, URLClassLoader cl, String className);
	void analysisComplete(Date completeTime);
}
