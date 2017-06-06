package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.RealEclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.SingleStore;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.project.BuildListener;
import com.gmmapowell.swimlane.eclipse.testrunner.RemoteJUnitTestRunner;

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
public class HexagonViewPart extends ViewPart implements SingleStore {
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private BuildListener bl;
	private ModelDispatcher dispatcher;
	private RemoteJUnitTestRunner tr;
	private Composite stackUI;
	private StackLayout stack;
	private HexView hexView;
	private ErrorView errorView;

	public void createPartControl(Composite parent) {
		RealEclipseAbstractor eclipse = new RealEclipseAbstractor();
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dispatcher = new SolidModelDispatcher();
		tr = new RemoteJUnitTestRunner(eclipse);
		new InfoBar(parent, dispatcher);
		stackUI = new Composite(parent, SWT.NONE);
		stackUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		stack = new StackLayout();
		stackUI.setLayout(stack);
		hexView = new HexView(stackUI, dispatcher);
		errorView = new ErrorView(stackUI, dispatcher);
		stack.topControl = hexView.getTop();
		try {
			bl = new BuildListener(dispatcher, eclipse);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(bl, IResourceChangeEvent.POST_BUILD);
		} catch (IllegalStateException ex) {
			// Unit tests will find the workspace closed, so cannot do this; this is OK in that case
			// TODO: how do we tell?
			bl = null;
		}
	}

	@Override
	public void showErrorPane() {
		stack.topControl = errorView.getTop();
		stackUI.layout();
	}

	@Override
	public void showHexPane() {
		stack.topControl = hexView.getTop();
		stackUI.layout();
	}

	@Override
	public void showTestResults(String id) {
		throw new RuntimeException("not implemented");
	}

	public void dispose() {
		if (bl != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(bl);
	}
	
	public void setFocus() {
	}

	@Override
	public TestRunner getTestRunner() {
		return tr;
	}

	@Override
	public ModelDispatcher getDispatcher() {
		return dispatcher;
	}

	@Override
	public Accumulator getAccumulator() {
		return hexView.getAccumulator();
	}

	public static SingleStore get(ExecutionEvent event) {
		IWorkbenchPart me = HandlerUtil.getActivePart(event);
		if (me == null)
			return null;
		if (!(me instanceof SingleStore))
			return null;
		return (SingleStore) me;
	}
}
