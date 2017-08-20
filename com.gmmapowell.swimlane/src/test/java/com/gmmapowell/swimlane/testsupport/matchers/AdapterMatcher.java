package com.gmmapowell.swimlane.testsupport.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.models.AdapterInfo;

public class AdapterMatcher extends TypeSafeMatcher<AdapterData>{
	private final String name;

	public AdapterMatcher(String name) {
		this.name = name;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("adapter for class ");
		arg0.appendValue(name);
	}

	@Override
	protected boolean matchesSafely(AdapterData arg0) {
		if (!(arg0 instanceof AdapterInfo))
			return false;
		AdapterInfo ai = (AdapterInfo)arg0;
		return ai.getName().equals(name);
	}

	public static AdapterMatcher adapter(Class<StringBuilder> cls) {
		// TODO Auto-generated method stub
		return new AdapterMatcher(cls.getName());
	}
}
