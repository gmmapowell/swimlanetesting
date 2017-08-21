package com.gmmapowell.swimlane.eclipse.models;

import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;

public class AdapterInfo extends BarInfo implements AdapterData {
	private final String name;

	public AdapterInfo(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void addTestListener(BarDataListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public String toString() {
		return "Adapter[" + name + "]";
	}
}
