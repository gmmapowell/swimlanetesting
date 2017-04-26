package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class HexagonAccumulator implements HexagonDataModel, Accumulator {
	private Date buildTime;
	private final List<BarData> acceptances = new ArrayList<BarData>();
	private final TotalOrder hexes = new TotalOrder();
	
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
	}
	
	@Override
	public void analysisComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub
		
	}
}
