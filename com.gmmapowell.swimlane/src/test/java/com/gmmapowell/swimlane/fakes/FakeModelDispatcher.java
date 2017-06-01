package com.gmmapowell.swimlane.fakes;

import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;

public class FakeModelDispatcher implements ModelDispatcher {
	private final ModelDispatcher mock;
	private final SolidModelDispatcher smd;

	public FakeModelDispatcher(ModelDispatcher mock) {
		this.mock = mock;
		smd = new SolidModelDispatcher();
	}
	
	public ModelDispatcher real() {
		return smd;
	}
	
	@Override
	public void addHexagonModelListener(HexagonModelListener lsnr) {
		smd.addHexagonModelListener(lsnr);
	}

	@Override
	public void addBarListener(BarData bar, BarDataListener lsnr) {
		mock.addBarListener(bar, lsnr);
		smd.addBarListener(bar, lsnr);
	}

	@Override
	public void removeBarListener(BarData bar, BarDataListener lsnr) {
		mock.removeBarListener(bar, lsnr);
		smd.removeBarListener(bar, lsnr);
	}

	@Override
	public void addAccumulator(AccumulatorListener lsnr) {
		smd.addAccumulator(lsnr);
	}

	@Override
	public void setModel(Object model) {
		smd.setModel(model);
	}

	@Override
	public void barChanged(BarData bar) {
		mock.barChanged(bar);
		smd.barChanged(bar);
	}

}
