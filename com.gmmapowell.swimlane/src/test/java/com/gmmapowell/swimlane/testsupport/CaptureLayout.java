package com.gmmapowell.swimlane.testsupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jmock.integration.junit4.JUnitRuleMockery;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;

public class CaptureLayout implements ViewLayout {
	private final JUnitRuleMockery context;
	public final List<BarDataListener> hexes = new ArrayList<>();
	public final List<BarDataListener> acceptance = new ArrayList<>();
	public BarDataListener utility;
	public final Map<String, BarDataListener> adapters = new TreeMap<>();
	
	public CaptureLayout(JUnitRuleMockery context) {
		this.context = context;
	}

	@Override
	public void addHexagon(int pos, BarData hi) {
		BarDataListener lsnr = context.mock(BarDataListener.class, "bdl_hex" + pos);

		while (hexes.size() <= pos)
			hexes.add(null);
		hexes.set(pos, lsnr);
		
		hi.addTestListener(lsnr);
	}

	@Override
	public void addAcceptance(int[] hexes, BarData bar) {
		BarDataListener lsnr = context.mock(BarDataListener.class, "bdl_acc" + acceptance.size());
		bar.addTestListener(lsnr);
		acceptance.add(lsnr);
	}

	@Override
	public void addHexagonPort(int pos, PortLocation loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAdapter(int hex, PortLocation ploc, int aloc, BarData adapter) {
		String name = "adapter_" + hex +"_"+ploc+"_"+aloc;
		BarDataListener lsnr = context.mock(BarDataListener.class, name);
		adapter.addTestListener(lsnr);
		adapters.put(name, lsnr);
	}


	@Override
	public void addUtility(BarData ud) {
		if (utility == null) {
			utility = context.mock(BarDataListener.class, "utility");
			ud.addTestListener(utility);
		}
	}
}
