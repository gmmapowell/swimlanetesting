package com.gmmapowell.swimlane.testsupport.matchers;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public abstract class RoleMatcher extends TypeSafeMatcher<TestRole> {
	protected final List<String> hexes;

	public RoleMatcher(String[] hexes) {
		this.hexes = Arrays.asList(hexes);
	}

	protected boolean matchesHexes(List<String> rh) {
		if (rh.size() != hexes.size())
			return false;
		for (String h : hexes)
			if (!rh.contains(h))
				return false;
		return true;
	}
}
