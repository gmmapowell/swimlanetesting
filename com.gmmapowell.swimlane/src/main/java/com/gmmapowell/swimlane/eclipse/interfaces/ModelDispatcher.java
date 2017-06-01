package com.gmmapowell.swimlane.eclipse.interfaces;

public interface ModelDispatcher {

	void addHexagonModelListener(HexagonModelListener lsnr);
	void addBarListener(BarData bar, BarDataListener lsnr);
	void removeBarListener(BarData bar, BarDataListener lsnr);
	void addAccumulator(AccumulatorListener lsnr);

	void setModel(Object model);
	void barChanged(BarData bar);

}