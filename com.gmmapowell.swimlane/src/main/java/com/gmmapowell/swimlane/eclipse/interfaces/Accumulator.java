package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public interface Accumulator {
	void setBuildTime(Date date);
	void testsCompleted(Date date);
	void acceptance(Class<?> tc, List<Class<?>> hexes);
	void error(String msg);
	void analysisComplete();
	List<TestGroup> getAllTestClasses();
}
