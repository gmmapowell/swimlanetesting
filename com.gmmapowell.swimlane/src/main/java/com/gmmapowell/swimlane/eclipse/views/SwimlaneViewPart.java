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
import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.CommandDispatcher;
import com.gmmapowell.swimlane.eclipse.models.ErrorCollector;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
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
public class SwimlaneViewPart extends ViewPart implements CommandDispatcher {
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private BuildListener bl;
	private RemoteJUnitTestRunner tr;
	private Composite stackUI;
	private StackLayout stack;
	private SwimlaneView hexView;
	private ErrorView errorView;
	private TestResultsView testResults;
	private SwimlaneModel centralModel;

	public void createPartControl(Composite parent) {
		RealEclipseAbstractor eclipse = new RealEclipseAbstractor();
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		new InfoBar(parent);
		stackUI = new Composite(parent, SWT.NONE);
		stackUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		stack = new StackLayout();
		stackUI.setLayout(stack);
		hexView = new SwimlaneView(stackUI);
		errorView = new ErrorView(stackUI);
		testResults = new TestResultsView(stackUI);
		stack.topControl = hexView.getTop();
		ErrorCollector errorcoll = new ErrorCollector();
		centralModel = new SwimlaneModel(errorcoll);
		try {
			HexagonTestAnalyzer hta = new HexagonTestAnalyzer(errorcoll, centralModel);
			bl = new BuildListener(eclipse, hta);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(bl, IResourceChangeEvent.POST_BUILD);
			bl.analyzeProjects(eclipse.getAllProjects());
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
			bl = null;
		}

		tr = new RemoteJUnitTestRunner(eclipse, centralModel);
	}

	@Override
	public void showErrorPane() {
		// TDA this ...
		stack.topControl = errorView.getTop();
		stackUI.layout();
	}

	@Override
	public void showHexPane() {
		// TDA this ...
		stack.topControl = hexView.getTop();
		stackUI.layout();
	}

	@Override
	public void showTestResults(String id) {
		// TDA this ...
		stack.topControl = testResults.getTop();
		stackUI.layout();
		if (id != null) // only update the current test results flag if something valid
			testResults.resultsFor(id);
		testResults.updateDisplay();
	}

	public void dispose() {
		if (bl != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(bl);
	}
	
	public void setFocus() {
	}

	
	@Override
	public void runAllTests() {
		centralModel.runAllTests(tr);
	}

	public static CommandDispatcher get(ExecutionEvent event) {
		IWorkbenchPart me = HandlerUtil.getActivePart(event);
		if (me == null)
			return null;
		if (!(me instanceof CommandDispatcher))
			return null;
		return (CommandDispatcher) me;
	}
}
