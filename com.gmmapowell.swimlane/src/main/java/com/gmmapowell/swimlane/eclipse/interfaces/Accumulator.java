package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

public interface Accumulator {
	void setBuildTime(Date date);
	void acceptance(Class<?> tc, List<Class<?>> hexes);
	void classError(String msg);
}
