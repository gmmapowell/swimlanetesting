package com.gmmapowell.swimlane.eclipse.roles;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class SingleHexRole implements TestRole {
	private final String hex;

	public SingleHexRole(Class<?> hex) {
		if (hex != null)
			this.hex = hex.getName();
		else
			this.hex = null;
	}

	public String getHex() {
		return hex;
	}

	@Override
	public List<String> getHexes() {
		List<String> ret = new ArrayList<>();
		ret.add(hex);
		return ret;
	}

}
