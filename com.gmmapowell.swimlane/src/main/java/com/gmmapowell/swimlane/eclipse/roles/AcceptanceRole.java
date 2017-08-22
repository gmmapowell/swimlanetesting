package com.gmmapowell.swimlane.eclipse.roles;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class AcceptanceRole implements TestRole {
	private final List<String> hexes;

	public AcceptanceRole(Class<?>[] hexes) {
		this.hexes = new ArrayList<>();
		for (Class<?> c : hexes)
			this.hexes.add(c.getName());
	}

	public List<String> getHexes() {
		return hexes;
	}
}
