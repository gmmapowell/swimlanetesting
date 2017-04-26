package com.gmmapowell.swimlane.eclipse.views;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;

public class InfoBar implements HexagonModelListener {
	private final SimpleDateFormat sdf;
	private final Label lastBuild;

	public InfoBar(Composite parent) {
		sdf = new SimpleDateFormat("HHmmss.SSS");

		Composite infoBar = new Composite(parent, SWT.NONE);
		infoBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		infoBar.setLayout(new FillLayout());
		lastBuild = new Label(infoBar, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
	}

	public void setModel(HexagonDataModel model) {
		lastBuild.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				lastBuild.setText(sdf.format(model.getBuildTime()));
			}
		});
	}
}
