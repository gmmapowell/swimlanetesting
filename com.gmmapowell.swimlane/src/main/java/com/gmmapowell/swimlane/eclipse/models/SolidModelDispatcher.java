package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class SolidModelDispatcher implements ModelDispatcher {
	private List<HexagonModelListener> modelLsnrs = new ArrayList<>();
	private List<BarDataListener> barLsnrs = new ArrayList<>();
	private List<AccumulatorListener> accLsnrs = new ArrayList<>();
	
	@Override
	public void addHexagonModelListener(HexagonModelListener lsnr) {
		modelLsnrs.add(lsnr);
	}

	@Override
	public void addBarListener(BarDataListener lsnr) {
		barLsnrs.add(lsnr);
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
		for (BarDataListener l : barLsnrs)
			l.barChanged(bar);
	}
}
