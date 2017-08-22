package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;

public class AcceptanceRoleMatcher extends RoleMatcher {

	private AcceptanceRoleMatcher(String... hexes) {
		super(hexes);
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("AcceptanceRole");
	}

	@Override
	protected boolean matchesSafely(TestRole arg0) {
		if (!(arg0 instanceof AcceptanceRole))
			return false;
		AcceptanceRole r = (AcceptanceRole) arg0;
		return matchesHexes(r.getHexes());
	}

	public static AcceptanceRoleMatcher withHexes(String... hexes) {
		return new AcceptanceRoleMatcher(hexes);
	}

}
