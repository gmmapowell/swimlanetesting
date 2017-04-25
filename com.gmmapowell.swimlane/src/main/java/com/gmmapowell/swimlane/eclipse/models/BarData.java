package com.gmmapowell.swimlane.eclipse.models;

import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel.Status;

public interface BarData {

	Status getStatus();

	int getComplete();

	int getTotal();

}
