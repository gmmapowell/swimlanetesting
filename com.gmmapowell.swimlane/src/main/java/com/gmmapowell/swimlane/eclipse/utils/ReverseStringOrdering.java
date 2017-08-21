package com.gmmapowell.swimlane.eclipse.utils;

import java.util.Comparator;

public class ReverseStringOrdering implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		return -o1.compareTo(o2);
	}

}
