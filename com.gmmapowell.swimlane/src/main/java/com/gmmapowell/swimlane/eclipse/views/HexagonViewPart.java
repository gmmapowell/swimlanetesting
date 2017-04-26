package com.gmmapowell.swimlane.eclipse.views;

import java.text.SimpleDateFormat;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.project.BuildListener;


public class HexagonViewPart extends ViewPart implements HexagonModelListener {
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";
	private final SimpleDateFormat sdf;

	private Label lastBuild;
	private HexView hexView;

	public HexagonViewPart() {
		sdf = new SimpleDateFormat("HHmmss.SSS");
	}

	public void createPartControl(Composite parent) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new BuildListener(this), IResourceChangeEvent.POST_BUILD);
		createControls(parent);
	}

	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		buildInfoBar(parent);
		hexView = new HexView(parent);
	}

	private void buildInfoBar(Composite parent) {
		Composite infoBar = new Composite(parent, SWT.NONE);
		infoBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		infoBar.setLayout(new FillLayout());
		lastBuild = new Label(infoBar, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
	}

	public void setFocus() {
	}

	public void setModel(HexagonDataModel model) {
		lastBuild.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				lastBuild.setText(sdf.format(model.getBuildTime()));
				hexView.update(model);
			}
		});
	}
}
