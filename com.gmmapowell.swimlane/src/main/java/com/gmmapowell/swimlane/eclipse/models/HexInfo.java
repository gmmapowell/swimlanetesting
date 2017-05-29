package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Collection;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class HexInfo implements HexData {

	private Collection<PortData> ports = new ArrayList<PortData>();
	{
		ports.add(new PortData() {
			
			@Override
			public PortLocation getLocation() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<BarData> getAdapters() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BarData getBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PortData> getPorts() {
		return ports;
	}

}
