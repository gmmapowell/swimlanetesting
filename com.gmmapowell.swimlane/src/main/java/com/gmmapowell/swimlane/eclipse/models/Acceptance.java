package com.gmmapowell.swimlane.eclipse.models;

import java.util.List;
import com.gmmapowell.swimlane.eclipse.interfaces.AcceptanceData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;

public class Acceptance extends BarInfo implements AcceptanceData {
	private List<String> hexs;
	private int[] marks;

	public Acceptance(List<String> hexs) {
		this.hexs = hexs;
	}


	@Override
	public void addTestListener(BarDataListener lsnr) {
		lsnrs.add(lsnr);
	}

	public void setMarks(List<String> order) {
		StringBuilder sb = new StringBuilder("acceptance.");
		marks = new int[order.size()];
		int i=0;
		for (String s : order) {
			int val = hexs.isEmpty() || hexs.contains(s)?1:0;
			marks[i++] = val;
			sb.append(val);
		}
		id = sb.toString();
	}

	@Override
	public int[] getMarks() {
		return marks;
	}

	@Override
	public String toString() {
		return "Acceptance" + classesUnderTest();
	}
}
