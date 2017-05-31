package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class SolidModelDispatcher implements ModelDispatcher {
	private List<HexagonModelListener> modelLsnrs = new ArrayList<>();
	private Map<String, Set<BarDataListener>> barLsnrs = new HashMap<>();
	private List<AccumulatorListener> accLsnrs = new ArrayList<>();
	
	@Override
	public void addHexagonModelListener(HexagonModelListener lsnr) {
		modelLsnrs.add(lsnr);
	}

	@Override
	public void addBarListener(BarData bar, BarDataListener lsnr) {
		if (bar == null)
			return;
		String id = bar.getId();
		if (!barLsnrs.containsKey(id))
			barLsnrs.put(id, new HashSet<>());
		barLsnrs.get(id).add(lsnr);
	}

	@Override
	public void addAccumulator(AccumulatorListener lsnr) {
		accLsnrs.add(lsnr);
	}

	@Override
	public void setModel(Object model) {
		if (model instanceof HexagonDataModel) {
			for (HexagonModelListener lsnr : modelLsnrs) {
				lsnr.setModel((HexagonDataModel) model);
			}
		}
		if (model instanceof Accumulator) {
			for (AccumulatorListener lsnr : accLsnrs) {
				lsnr.setModel((Accumulator) model);
			}
		}
	}

	@Override
	public void barChanged(BarData bar) {
		Set<BarDataListener> lsnrs = barLsnrs.get(bar.getId());
		if (lsnrs != null)
			for (BarDataListener l : lsnrs)
				l.barChanged(bar);
	}
}
