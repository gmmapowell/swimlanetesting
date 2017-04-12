package com.gmmapowell.swimlane.tests.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ClassMatcher extends BaseMatcher<Class<?>> {
	private final String clzName;

	private ClassMatcher(String clzName) {
		this.clzName = clzName;
	}

	public static <T> ClassMatcher named(String clzName) {
		return new ClassMatcher(clzName);
	}

	@Override
	public boolean matches(Object item) {
		return item instanceof Class && ((Class<?>)item).getName().equals(clzName);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("class of type ");
		description.appendValue(clzName);
	}
}
