package com.gmmapowell.swimlane.eclipse.popos;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;

public class TestHolder implements Accumulator{
	public final List<String> msgs = new ArrayList<String>();

	public void acceptance(Class<?> elt) {
		msgs.add(elt.getName());
	}
}
