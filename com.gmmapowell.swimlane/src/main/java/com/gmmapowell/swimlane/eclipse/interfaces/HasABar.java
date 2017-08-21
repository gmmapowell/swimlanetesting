package com.gmmapowell.swimlane.eclipse.interfaces;

import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public interface HasABar {

	void clearGroup(GroupOfTests grp);

	void testCompleted(TestCaseInfo testCaseInfo);

}
