package com.gmmapowell.swimlane.eclipse.interfaces;

public interface GroupTraverser {
	void group(GroupOfTests grp);
	void testClass(String testClassName);
	void testCase(String string, TestInfo tci);
}
