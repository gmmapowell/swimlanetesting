package com.gmmapowell.swimlane.tests.accumulator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.models.BarInfo;

public class BarInfoMatcher extends TypeSafeMatcher<BarInfo> {

	private final boolean passing;
	private final int complete;
	private final int total;

	public BarInfoMatcher(boolean passing, int complete, int total) {
		this.passing = passing;
		this.complete = complete;
		this.total = total;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("bar info ");
		if (passing)
			arg0.appendText("passing");
		else
			arg0.appendText("failing");
		arg0.appendText(" ");
		arg0.appendValue(complete);
		arg0.appendText("/");
		arg0.appendValue(total);
	}

	@Override
	protected boolean matchesSafely(BarInfo arg0) {
		if (passing != arg0.isPassing())
			return false;
		if (complete != arg0.getComplete())
			return false;
		if (total != arg0.getTotal())
			return false;
		return true;
	}

	public static BarInfoMatcher passing(int complete, int total) {
		return new BarInfoMatcher(true, complete, total);
	}

	public static BarInfoMatcher failing(int complete, int total) {
		return new BarInfoMatcher(false, complete, total);
	}

}
