package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;

// TODO: I'm not really happy with this name ...
public interface DataCentral {
	public interface GroupHandler {
		public IStatus runGroup(GroupOfTests grp);
	}

	void addBuildDateListener(DateListener lsnr);
	void addTestDateListener(DateListener lsnr);

	AnalysisAccumulator startAnalysis(Date startTime);
	void visitGroups(GroupHandler hdlr);
	void testsRun(Date date);
}
