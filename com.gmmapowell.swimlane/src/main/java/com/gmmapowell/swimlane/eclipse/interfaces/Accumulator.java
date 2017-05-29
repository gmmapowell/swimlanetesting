package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public interface Accumulator {
	void setBuildTime(Date date);
	void testsCompleted(Date date);
	void acceptance(TestGroup grp, Class<?> tc, List<Class<?>> hexes);
	void adapter(TestGroup grp, Class<?> tc, Class<?> hex, Class<?> port);
	void error(String msg);
	void analysisComplete();
	List<TestGroup> getAllTestClasses();
}
