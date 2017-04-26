package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

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

	private BuildListener bl;
	private InfoBar infoBar;
	private HexView hexView;

	public void createPartControl(Composite parent) {
		bl = new BuildListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(bl, IResourceChangeEvent.POST_BUILD);
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		infoBar = new InfoBar(parent);
		hexView = new HexView(parent);
		bl.addListener(infoBar);
		bl.addListener(hexView);
	}

	public void dispose() {
		if (bl != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(bl);
	}
	
	public void setFocus() {
	}
}
