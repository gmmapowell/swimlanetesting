package com.gmmapowell.swimlane.eclipse.interfaces;

import org.eclipse.core.runtime.IStatus;

public interface DataCentral {

	public interface GroupHandler {
		public IStatus runGroup(GroupOfTests grp);
	}

	void allGroups(GroupHandler hdlr);

}
