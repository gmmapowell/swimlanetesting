package com.gmmapowell.swimlane.testsupport.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.analyzer.BusinessRole;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;

public class BusinessRoleMatcher extends RoleMatcher {

	private BusinessRoleMatcher(String hex) {
		super(hex==null?new String[0]:new String[] {hex});
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("BusinessRole");
		if (!hexes.isEmpty()) {
			arg0.appendText(" in ");
			arg0.appendValue(hexes.get(0));
		}
	}

	@Override
	protected boolean matchesSafely(TestRole arg0) {
		if (!(arg0 instanceof BusinessRole))
			return false;
		BusinessRole r = (BusinessRole) arg0;
		List<String> hexes = new ArrayList<>();
		String hex = r.getHex();
		if (hex != null)
			hexes.add(hex);
		return matchesHexes(hexes);
	}

	public static BusinessRoleMatcher logic(String hex) {
		return new BusinessRoleMatcher(hex);
	}

}
