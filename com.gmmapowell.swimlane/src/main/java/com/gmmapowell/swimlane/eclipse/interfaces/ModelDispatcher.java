package com.gmmapowell.swimlane.eclipse.interfaces;

public interface ModelDispatcher {

	void addHMD(HexagonModelListener lsnr);

	void addAccumulator(AccumulatorListener lsnr);

	void setModel(Object model);

}