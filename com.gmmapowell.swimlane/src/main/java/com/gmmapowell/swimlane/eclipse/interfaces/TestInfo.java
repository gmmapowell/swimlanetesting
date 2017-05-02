package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface TestInfo {
	String testName();
	boolean hasFailed();
	List<String> stack();
}
