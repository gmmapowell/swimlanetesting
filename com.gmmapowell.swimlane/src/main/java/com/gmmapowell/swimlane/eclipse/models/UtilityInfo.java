package com.gmmapowell.swimlane.eclipse.models;

import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.UtilityData;

public class UtilityInfo extends BarInfo implements UtilityData {

	public UtilityInfo() {
	}

	@Override
	public void addTestListener(BarDataListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public String toString() {
		return "Utility" + classesUnderTest();
	}
}
