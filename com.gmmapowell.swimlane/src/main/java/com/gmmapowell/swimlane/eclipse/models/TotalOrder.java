package com.gmmapowell.swimlane.eclipse.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalOrder {
	public enum Order {
		BEFORE, SAME, AFTER
	}

	private boolean haveDefault = false;
	private Map<String, Map<String, Order>> ordering = new HashMap<String, Map<String, Order>>();

	public void haveDefault() {
		haveDefault = true;
	}

	public void addAll(List<Class<?>> hexes) {
		for (Class<?> cls : hexes) {
			String name = cls.getName();
			if (!ordering.containsKey(name)) {
				ordering.put(name, new HashMap<>());
			}
		}
	}

	public int count() {
		if (!ordering.isEmpty())
			return ordering.size();
		return haveDefault?1:0;
	}
}
