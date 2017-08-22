package com.gmmapowell.swimlane.eclipse.analyzer;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class BusinessRole implements TestRole {
	private final String hex;

	public BusinessRole(Class<?> hex) {
		if (hex != null)
			this.hex = hex.getName();
		else
			this.hex = null;
	}

	public String getHex() {
		return hex;
	}

}
