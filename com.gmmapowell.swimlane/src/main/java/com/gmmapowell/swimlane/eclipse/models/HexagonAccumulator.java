package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class HexagonAccumulator implements HexagonDataModel, Accumulator {
	private Date buildTime;
	private final Map<String, Acceptance> compileAcceptances = new TreeMap<String, Acceptance>();
	private List<BarData> acceptances = new ArrayList<>();
	private final TotalOrder hexes = new TotalOrder();
	private List<String> errors = new ArrayList<>();
	
	public void setBuildTime(Date d) {
		this.buildTime = d;
	}
	
	public Date getBuildTime() {
		return this.buildTime;
	}
	
	@Override
	public int getHexCount() {
		return hexes.count();
	}

	@Override
	public List<BarData> getAcceptanceTests() {
		return acceptances;
	}

	@Override
	public void acceptance(Class<?> tc, List<Class<?>> hexes) {
		if (hexes == null || hexes.isEmpty())
			this.hexes.haveDefault();
		else
			this.hexes.addAll(hexes);
		List<String> names = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (Class<?> c : hexes) {
			names.add(c.getName());
			if (sb.length() > 0)
				sb.append("||");
			sb.append(c.getName());
		}
		String an = sb.toString();
		Acceptance ca = compileAcceptances.get(an);
		if (ca == null) {
			ca = new Acceptance(names);
			compileAcceptances.put(an, ca);
		}
		ca.addCase(tc);
	}
	
	@Override
	public void analysisComplete() {
		errors.addAll(this.hexes.ensureTotalOrdering());
		TreeMap<String, Acceptance> tmp = new TreeMap<String, Acceptance>();
		List<String> order = this.hexes.bestOrdering();
		for (Acceptance a : compileAcceptances.values()) {
			a.setMarks(order);
			tmp.put(a.getId(), a);
		}
		// TODO: order this based on the final total ordering
		// TODO: also name them
		for (Acceptance a : compileAcceptances.values()) {
			acceptances.add(a);
		}
	}

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getErrors() {
		return errors;
	}
}
