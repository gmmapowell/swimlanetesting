package com.gmmapowell.swimlane.tests.accumulator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.models.HexInfo;

public class HexInfoMatcher extends TypeSafeMatcher<HexInfo> {
	private final String clz;

	public HexInfoMatcher(String clz) {
		this.clz = clz;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("HexInfo called " + clz);
	}

	@Override
	protected boolean matchesSafely(HexInfo arg0) {
		if (!arg0.getName().equals(clz))
			return false;
		return true;
	}

	public static HexInfoMatcher called(Class<?> clz) {
		return new HexInfoMatcher(clz == null ? "" : clz.getName());
	}

}
