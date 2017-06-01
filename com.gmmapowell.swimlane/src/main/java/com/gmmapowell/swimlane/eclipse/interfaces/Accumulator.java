package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public interface Accumulator {
	void setBuildTime(Date date);
	void testsCompleted(Date date);
	void acceptance(TestGroup grp, Class<?> tc, List<Class<?>> hexes);
	void logic(TestGroup grp, Class<?> testCase1, Class<?> hexClass1);
	void adapter(TestGroup grp, Class<?> tc, Class<?> hex, Class<?> port, Class<?> adapter);
//	void portLocation(Class<?> hexClass, Class<?> portClass, PortLocation loc);
	void portLocation(Class<?> adapterClass, PortLocation loc);
	void utility(TestGroup grp, Class<?> testCase1);
	void error(String msg);
	void analysisComplete();
	List<TestGroup> getAllTestGroups();
}
