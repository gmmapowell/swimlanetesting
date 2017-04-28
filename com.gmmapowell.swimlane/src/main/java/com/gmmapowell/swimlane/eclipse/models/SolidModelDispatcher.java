package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class SolidModelDispatcher implements ModelDispatcher {
	private List<HexagonModelListener> hmdLsnrs = new ArrayList<>();
	private List<AccumulatorListener> accLsnrs = new ArrayList<>();
	
	@Override
	public void addHMD(HexagonModelListener lsnr) {
		hmdLsnrs.add(lsnr);
	}

	@Override
	public void addAccumulator(AccumulatorListener lsnr) {
		accLsnrs.add(lsnr);
	}

	@Override
	public void setModel(Object model) {
		if (model instanceof HexagonDataModel) {
			for (HexagonModelListener lsnr : hmdLsnrs) {
				lsnr.setModel((HexagonDataModel) model);
			}
		}
		if (model instanceof Accumulator) {
			for (AccumulatorListener lsnr : accLsnrs) {
				lsnr.setModel((Accumulator) model);
			}
		}
	}

}
