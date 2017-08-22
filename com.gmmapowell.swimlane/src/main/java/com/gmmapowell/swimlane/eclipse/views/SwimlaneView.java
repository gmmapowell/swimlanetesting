package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewComponentFactory;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;

public class SwimlaneView implements ViewLayout {
	private final ViewComponentFactory factory;
	private final Composite view;

	public SwimlaneView(Composite parent, ViewComponentFactory factory) {
		this.factory = factory;
		view = new Composite(parent, SWT.NONE);
		view.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SwimlaneLayout layout = new SwimlaneLayout();
		view.setLayout(layout);
	}

	public Control getTop() {
		return view;
	}

	@Override
	public void addHexagon(int pos, BarData hi) {
		HexagonBackground hc = factory.hexagon(view, pos);
		BarControl bc = factory.bar(view, "business." + pos);
		bc.getCanvas().setLayoutData(new BusinessBarLayout(hc, bc.getCanvas()));
		hi.addTestListener(bc);
		view.layout();
	}

	@Override
	public void addAcceptance(int[] hexes, BarData ad) {
		String mask = maskString(hexes);
		BarControl bc = factory.bar(view, "acceptance." + mask);
		bc.getCanvas().setLayoutData(new AcceptanceBarLayout(mask, bc.getCanvas()));
		ad.addTestListener(bc);
		view.layout();
	}
	
	@Override
	public void addHexagonPort(int pos, PortLocation loc) {
		factory.port(view, pos, loc);
		view.layout();
	}

	@Override
	public void addAdapter(int hex, PortLocation ploc, int aloc, BarData adapter) {
		BarControl bc = factory.bar(view, "adapter." + hex + "." + ploc + "." + aloc);
		bc.getCanvas().setLayoutData(new AdapterBarLayout(hex, ploc, aloc, bc.getCanvas()));
		adapter.addTestListener(bc);
		view.layout();
	}

	@Override
	public void addUtility(BarData ad) {
		BarControl bc = factory.bar(view, "utility");
		bc.getCanvas().setLayoutData(new UtilityBarLayout(bc.getCanvas()));
		ad.addTestListener(bc);
		view.layout();
	}

	private String maskString(int[] hexes) {
		StringBuilder sb = new StringBuilder();
		for (int i : hexes) {
			sb.append(i);
		}
		return sb.toString();
	}
}
