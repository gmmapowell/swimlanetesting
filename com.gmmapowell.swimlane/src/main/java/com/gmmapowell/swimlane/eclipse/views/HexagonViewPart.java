package com.gmmapowell.swimlane.eclipse.views;

import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.popos.TestHolder;
import com.gmmapowell.swimlane.eclipse.project.ProjectHelper;
import com.gmmapowell.swimlane.eclipse.project.ProjectScanner;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class HexagonViewPart extends ViewPart implements IResourceChangeListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private Label lastBuild;
	private final SimpleDateFormat sdf;

	private Canvas acceptance;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	/**
	 * The constructor.
	 */
	public HexagonViewPart() {
		sdf = new SimpleDateFormat("HHmmss.SSS");
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);
		createControls(parent);
	}

	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lastBuild = new Label(parent, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
		acceptance = new Canvas(parent, SWT.NONE);
		acceptance.setData("org.eclipse.swtbot.widget.key", "hexagons.acceptance.1");
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 6;
		acceptance.setLayoutData(gd);
		acceptance.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				GC gc = new GC((Drawable) e.widget);
				Color green = parent.getDisplay().getSystemColor(SWT.COLOR_GREEN);
				Color grey = parent.getDisplay().getSystemColor(SWT.COLOR_GRAY);
				gc.setBackground(green);
				gc.fillRectangle(e.x, e.y, e.width/2, e.height);
				gc.setBackground(grey);
				gc.fillRectangle(e.x+e.width/2, e.y, e.width/2, e.height);
				gc.dispose();
			}
		});
	}

	public void setFocus() {
	}

	public void setModel(HexagonDataModel model) {
		lastBuild.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				lastBuild.setText(sdf.format(model.getBuildTime()));
			}
		});
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		HexagonDataModel model = new HexagonDataModel();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(SWT.NONE);
		for (IProject p : projects) {
			IJavaProject jp = JavaCore.create(p);
			if (jp != null) {
				try {
					ProjectHelper ph = new ProjectHelper(jp);
					URLClassLoader cl = ph.deduceClasspath();
					Accumulator tests = new TestHolder();
					ProjectScanner scanner = new ProjectScanner(ph, new HexagonTestAnalyzer(cl, tests));
					scanner.scan(jp);
				} catch (JavaModelException e) {
					// TODO: we should capture "problems" with the view
					e.printStackTrace();
				}
			}
		}

		model.setBuildTime(new Date());
		setModel(model);
	}
}
