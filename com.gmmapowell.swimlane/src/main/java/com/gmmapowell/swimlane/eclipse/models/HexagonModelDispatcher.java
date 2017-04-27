package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;

public class HexagonModelDispatcher implements HexagonModelListener {
	private List<HexagonModelListener> lsnrs = new ArrayList<>();
	
	public void add(HexagonModelListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public void setModel(HexagonDataModel model) {
		for (HexagonModelListener lsnr : lsnrs) {
			lsnr.setModel(model);
		}
	}

}
