package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.project.BuildListener;


public class HexagonViewPart extends ViewPart {
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private InfoBar infoBar;
	private HexView hexView;

	public void createPartControl(Composite parent) {
		BuildListener bl = new BuildListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(bl, IResourceChangeEvent.POST_BUILD);
		createControls(parent);
		bl.addListener(infoBar);
		bl.addListener(hexView);
	}

	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		infoBar = new InfoBar(parent);
		hexView = new HexView(parent);
	}

	public void setFocus() {
	}
}
