package com.gmmapowell.swimlane.testsupport.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;

public class AdapterRoleMatcher extends RoleMatcher {
	private String port;
	private String adapter;
	private PortLocation location;

	private AdapterRoleMatcher(String hex) {
		super(hex==null?new String[0]:new String[] {hex});
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("AdapterRole");
		if (!hexes.isEmpty()) {
			arg0.appendText(" in ");
			arg0.appendValue(hexes.get(0));
		}
		if (port != null) {
			arg0.appendText(" using port ");
			arg0.appendValue(port);
		}
		if (adapter != null) {
			arg0.appendText(" with adapter ");
			arg0.appendValue(adapter);
		}
		if (location != null) {
			arg0.appendText(" at ");
			arg0.appendValue(location);
		}
	}

	@Override
	protected boolean matchesSafely(TestRole arg0) {
		if (!(arg0 instanceof AdapterRole))
			return false;
		AdapterRole r = (AdapterRole) arg0;
		List<String> hexes = new ArrayList<>();
		String hex = r.getHex();
		if (hex != null)
			hexes.add(hex);
		if (!matchesHexes(hexes))
			return false;
		String ap = r.getPort();
		if (port == null) {
			if (ap != null) return false;
		} else {
			if (!port.equals(ap))
				return false;
		}
		String aa = r.getAdapter();
		if (adapter == null) {
			if (aa != null) return false;
		} else {
			if (!adapter.equals(aa))
				return false;
		}
		PortLocation loc = r.getLocation();
		if (location == null) {
			if (loc != null) return false;
		} else {
			if (location != loc)
				return false;
		}
		return true;
	}

	public static AdapterRoleMatcher hex(String hex) {
		return new AdapterRoleMatcher(hex);
	}

	public AdapterRoleMatcher port(String port) {
		this.port = port;
		return this;
	}

	public AdapterRoleMatcher adapter(String adapter) {
		this.adapter = adapter;
		return this;
	}
	
	public AdapterRoleMatcher location(PortLocation pl) {
		this.location = pl;
		return this;
	}

}
