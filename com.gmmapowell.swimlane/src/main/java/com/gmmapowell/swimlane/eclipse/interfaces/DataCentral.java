package com.gmmapowell.swimlane.eclipse.interfaces;

import org.eclipse.core.runtime.IStatus;

public interface DataCentral {
	public interface GroupHandler {
		public IStatus runGroup(GroupOfTests grp);
	}

	void addBuildDateListener(DateListener lsnr);
	void addErrorMessageListener(ErrorMessageListener eml);
	void setSolution(Solution solution);

	void allGroups(GroupHandler hdlr);
}
