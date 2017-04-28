package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.project.BuildListener;

/* We are really looking at a pipeline here.
 * This view part "listens" to post build events and then uses that to collect a (static) description of all the system's tests,
 *   which is basically all the "class" files which are appropriately annotated and messages about conflicts;
 *   this is sufficient to build the diagram's structure
 *     - there is a ProjectScanner, which looks through all the .class files in the project
 *     - there is a HexagonTestAnalyzer, which analyzes each class
 *     - there is a HexagonAccumulator (behind the Accumulator interface) which assembles and organizes them
 * From time to time, it then runs the various groups of tests, either in the background or with user consent
 * From this, it builds a "dynamic" view of the tests, i.e. quantity, names and (current) results
 * These are then put together with the static view to build and color the bars within the various portions of the static layout
 */
public class HexagonViewPart extends ViewPart {
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";
	public static final String RunAllID = "com.gmmapowell.swimlane.actions.RunAllTests";

	private BuildListener bl;
	private ModelDispatcher lsnrs;

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lsnrs = new SolidModelDispatcher();
		new InfoBar(parent, lsnrs);
		new HexView(parent, lsnrs);
		configureToolbar(getViewSite().getActionBars().getToolBarManager(), lsnrs);
		try {
			bl = new BuildListener(lsnrs);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(bl, IResourceChangeEvent.POST_BUILD);
		} catch (IllegalStateException ex) {
			// Unit tests will find the workspace closed, so cannot do this; this is OK in that case
			// TODO: how do we tell?
			bl = null;
		}
	}

	public void configureToolbar(IToolBarManager toolBar, ModelDispatcher lsnrs) {
		RunAllTestsAction rata = new RunAllTestsAction();
		lsnrs.addAccumulator(rata);
		toolBar.add(rata);
	}

	public void dispose() {
		if (bl != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(bl);
	}
	
	public void setFocus() {
	}
}
