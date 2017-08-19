package com.gmmapowell.swimlane.eclipse.interfaces;

public interface ErrorAccumulator {
	void addErrorMessageListener(ErrorMessageListener eml);
	void error(String message);
}
