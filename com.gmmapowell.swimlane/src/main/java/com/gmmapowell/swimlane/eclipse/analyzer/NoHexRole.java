package com.gmmapowell.swimlane.eclipse.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class NoHexRole implements TestRole {

	@Override
	public List<String> getHexes() {
		return new ArrayList<>();
	}

}
