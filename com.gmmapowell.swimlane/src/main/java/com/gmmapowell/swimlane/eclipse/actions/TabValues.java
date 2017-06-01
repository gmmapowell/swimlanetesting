package com.gmmapowell.swimlane.eclipse.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

public class TabValues implements IParameterValues {

	@Override
	public Map<String, Object> getParameterValues() {
		Map<String, Object> map = new HashMap<>();
		map.put("Hexes", "Hexes");
		map.put("Errors", "Errors");
		return map;
	}

}
