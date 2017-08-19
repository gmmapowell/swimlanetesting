package com.gmmapowell.swimlane.eclipse.interfaces;

import org.eclipse.core.runtime.IStatus;

public interface DataCentral {
	public interface GroupHandler {
		public IStatus runGroup(GroupOfTests grp);
	}

	void addBuildDateListener(DateListener lsnr);

	void allGroups(GroupHandler hdlr);
}
