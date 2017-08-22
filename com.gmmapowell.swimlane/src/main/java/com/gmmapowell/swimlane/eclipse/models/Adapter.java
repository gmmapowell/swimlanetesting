package com.gmmapowell.swimlane.eclipse.models;

public class Adapter extends BarInfo {
	private final Class<?> adapterUnderTest;

	public Adapter(Class<?> adapterUnderTest) {
		this.adapterUnderTest = adapterUnderTest;
	}

	public boolean forAdapter(Class<?> adapter) {
		return adapter.equals(adapterUnderTest);
	}
	
	public String toString() {
		return "Adapter";
	}
}
