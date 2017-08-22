package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class UtilityRoleMatcher extends TypeSafeMatcher<TestRole> {

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("UtilityRole");
	}

	@Override
	protected boolean matchesSafely(TestRole arg0) {
		if (!(arg0 instanceof UtilityRole))
			return false;
		return true;
	}

	public static UtilityRoleMatcher matcher() {
		return new UtilityRoleMatcher();
	}

}
