package com.gmmapowell.swimlane.eclipse.interfaces;

public interface BarDataListener {
	void clearGroup(GroupOfTests grp);
	void testCompleted(GroupOfTests grp, TestInfo ti);
}
