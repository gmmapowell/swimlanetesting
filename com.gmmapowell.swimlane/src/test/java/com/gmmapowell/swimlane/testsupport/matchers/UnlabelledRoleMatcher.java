package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.roles.UnlabelledTestRole;

public class UnlabelledRoleMatcher extends TypeSafeMatcher<TestRole> {

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("UnlabelledRole");
	}

	@Override
	protected boolean matchesSafely(TestRole arg0) {
		if (!(arg0 instanceof UnlabelledTestRole))
			return false;
		return true;
	}

	public static UnlabelledRoleMatcher matcher() {
		return new UnlabelledRoleMatcher();
	}

}
