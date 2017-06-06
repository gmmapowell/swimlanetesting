package com.gmmapowell.swimlane.eclipse.models;

public class LogicInfo extends BarInfo {
	private String clz;

	public LogicInfo(String clz, String id) {
		this.clz = clz;
		this.id = id;
	}

	@Override
	public String getName() {
		return clz;
	}
}
