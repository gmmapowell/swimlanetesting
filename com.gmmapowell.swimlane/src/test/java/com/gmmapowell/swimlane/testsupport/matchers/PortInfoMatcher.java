package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.models.PortInfo;

public class PortInfoMatcher extends TypeSafeMatcher<PortInfo> {
	private final PortLocation loc;
	private final String clz;

	public PortInfoMatcher(PortLocation loc, String clz) {
		this.loc = loc;
		this.clz = clz;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("PortInfo");
		if (loc != null) {
			arg0.appendText(" in ");
			arg0.appendValue(loc);
		}
		arg0.appendText(" with ");
		arg0.appendValue(clz);
	}

	@Override
	protected boolean matchesSafely(PortInfo arg0) {
		if (loc != null && arg0.getLocation() != loc)
			return false;
		if (!arg0.getName().equals(clz))
			return false;
		return true;
	}

	public static PortInfoMatcher port(PortLocation loc, Class<?> clz) {
		return new PortInfoMatcher(loc, clz.getName());
	}

	public static PortInfoMatcher port(Class<?> clz) {
		return new PortInfoMatcher(null, clz.getName());
	}

}
