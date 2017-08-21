package com.gmmapowell.swimlane.testsupport;

import java.util.ArrayList;
import java.util.List;

import org.jmock.integration.junit4.JUnitRuleMockery;

import com.gmmapowell.swimlane.eclipse.interfaces.AcceptanceData;
import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.UtilityData;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;

public class CaptureLayout implements ViewLayout {
	private final JUnitRuleMockery context;
	public final List<BarDataListener> hexes = new ArrayList<>();
	public final List<BarDataListener> acceptance = new ArrayList<>();
	public BarDataListener utility;
	
	public CaptureLayout(JUnitRuleMockery context) {
		this.context = context;
	}

	@Override
	public void addHexagon(int pos, HexData hi) {
		BarDataListener lsnr = context.mock(BarDataListener.class, "bdl_hex" + pos);

		while (hexes.size() <= pos)
			hexes.add(null);
		hexes.set(pos, lsnr);
		
		hi.addBusinessLogicListener(lsnr);
	}

	@Override
	public void addAcceptance(int[] hexes, AcceptanceData ad) {
		BarDataListener lsnr = context.mock(BarDataListener.class, "bdl_acc" + acceptance.size());
		ad.addTestListener(lsnr);
		acceptance.add(lsnr);
	}

	@Override
	public void addHexagonPort(int pos, PortLocation loc, PortData port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAdapter(int hex, PortLocation ploc, int aloc, AdapterData adapter) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addUtility(UtilityData ud) {
		if (utility == null) {
			utility = context.mock(BarDataListener.class, "utility");
			ud.addTestListener(utility);
		}
	}
}
