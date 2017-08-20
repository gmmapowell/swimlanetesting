package com.gmmapowell.swimlane.eclipse.models;

import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;

public class AdapterInfo implements AdapterData {
	private final String name;

	public AdapterInfo(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Adapter[" + name + "]";
	}
}
