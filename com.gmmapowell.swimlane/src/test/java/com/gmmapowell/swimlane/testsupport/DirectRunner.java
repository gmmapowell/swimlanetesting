package com.gmmapowell.swimlane.testsupport;

import com.gmmapowell.swimlane.eclipse.interfaces.ScreenSync;

public class DirectRunner implements ScreenSync {

	@Override
	public void syncExec(Runnable r) {
		r.run();
	}

}
