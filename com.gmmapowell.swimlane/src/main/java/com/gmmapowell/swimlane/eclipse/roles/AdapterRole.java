package com.gmmapowell.swimlane.eclipse.roles;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class AdapterRole extends SingleHexRole {
	private final String port;
	private final PortLocation loc;
	private final String adapter;

	public AdapterRole(Class<?> hex, Class<?> port, PortLocation pl, Class<?> adapter) {
		super(hex);
		if (port != null)
			this.port = port.getName();
		else
			this.port = null;
		this.loc = pl;
		if (adapter != null)
			this.adapter = adapter.getName();
		else
			this.adapter = null;
	}

	public String getPort() {
		return port;
	}

	public PortLocation getLocation() {
		return loc;
	}

	public String getAdapter() {
		return adapter;
	}

}
