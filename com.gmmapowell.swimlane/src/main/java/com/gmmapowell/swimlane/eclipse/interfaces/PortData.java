package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Collection;

public interface PortData {

	PortLocation getLocation();
	Collection<AdapterData> getAdapters();

}
