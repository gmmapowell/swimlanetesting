package com.gmmapowell.swimlane.eclipse.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;

public class InfoBar {
	private final SimpleDateFormat sdf;
	private final Label lastBuild;
	private final Label testsComplete;

	public InfoBar(Composite parent, DataCentral model) {
		sdf = new SimpleDateFormat("HHmmss.SSS");

		Composite infoBar = new Composite(parent, SWT.NONE);
		infoBar.moveAbove(null);
		infoBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		infoBar.setLayout(new FillLayout());
		lastBuild = new Label(infoBar, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "swimlane.lastBuild");
		lastBuild.setText("none");
		testsComplete = new Label(infoBar, SWT.NONE);
		testsComplete.setData("org.eclipse.swtbot.widget.key", "swimlane.testsComplete");
		testsComplete.setText("none");
		
		model.addBuildDateListener(date -> parent.getDisplay().syncExec(() -> lastBuild.setText(formatDate(date))));
		model.addTestDateListener(date -> parent.getDisplay().syncExec(() -> testsComplete.setText(formatDate(date))));
	}

	protected String formatDate(Date tmp) {
		if (tmp == null)
			return "";
		return sdf.format(tmp);
	}
}
