package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;

import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;

// TODO: I'm not really happy with this name ...
public interface DataCentral {
	public interface GroupHandler {
		public IStatus runGroup(GroupOfTests grp);
	}

	void addBuildDateListener(DateListener lsnr);

	void allGroups(GroupHandler hdlr);

	SolutionCreator startAnalysis(Date startTime);
}
