package com.gmmapowell.swimlane.eclipse.interfaces;

public interface BarData {
	void addTestListener(BarDataListener lsnr);
	int getTotal();
	int getComplete();
	boolean isPassing();
	String getTooltip(String name);
	void traverseTree(GroupTraverser traverser);
}
