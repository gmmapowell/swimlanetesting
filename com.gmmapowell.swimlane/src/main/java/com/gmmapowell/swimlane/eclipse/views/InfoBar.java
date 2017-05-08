package com.gmmapowell.swimlane.eclipse.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class InfoBar implements HexagonModelListener {
	private final SimpleDateFormat sdf;
	private final Label lastBuild;
	private final Label testsComplete;

	public InfoBar(Composite parent, ModelDispatcher lsnrs) {
		sdf = new SimpleDateFormat("HHmmss.SSS");

		Composite infoBar = new Composite(parent, SWT.NONE);
		infoBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		infoBar.setLayout(new FillLayout());
		lastBuild = new Label(infoBar, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
		testsComplete = new Label(infoBar, SWT.NONE);
		testsComplete.setData("org.eclipse.swtbot.widget.key", "hexagons.testsComplete");
		testsComplete.setText("none");

		lsnrs.addHexagonModelListener(this);
	}

	public void setModel(HexagonDataModel model) {
		lastBuild.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				lastBuild.setText(formatDate(model.getBuildTime()));
				testsComplete.setText(formatDate(model.getTestCompleteTime()));
			}
		});
	}

	protected String formatDate(Date tmp) {
		if (tmp == null)
			return "";
		return sdf.format(tmp);
	}
}
