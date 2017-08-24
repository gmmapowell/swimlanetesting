package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IntArrayMatcher extends TypeSafeMatcher<int[]> {
	private int[] vs;

	public IntArrayMatcher(int[] vs) {
		this.vs = vs;
	}

	@Override
	public void describeTo(Description arg0) {
		for (int i=0;i<vs.length;i++)
			arg0.appendValue(vs[i]);
	}

	@Override
	protected boolean matchesSafely(int[] arg0) {
		if (arg0 == null)
			return false;
		if (arg0.length != vs.length)
			return false;
		for (int i=0;i<vs.length;i++)
			if (vs[i] != arg0[i])
				return false;
		return true;
	}

	public static IntArrayMatcher with(int... vs) {
		return new IntArrayMatcher(vs);
	}

}
