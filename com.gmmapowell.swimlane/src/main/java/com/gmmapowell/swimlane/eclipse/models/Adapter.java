package com.gmmapowell.swimlane.eclipse.models;

public class Adapter extends BarInfo {
	private final Class<?> adapterUnderTest;

	public Adapter(Class<?> adapterUnderTest) {
		this.adapterUnderTest = adapterUnderTest;
		this.id = "adapter." + adapterUnderTest.getName();
	}

	@Override
	public String getName() {
		return adapterUnderTest.getName();
	}

	public boolean forAdapter(Class<?> adapter) {
		return adapter.equals(adapterUnderTest);
	}
	
	public String toString() {
		return "Adapter" + classesUnderTest();
	}
}
