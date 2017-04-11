package com.gmmapowell.swimlane.eclipse.views;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


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

public class HexagonView extends ViewPart implements IResourceChangeListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private ViewContentProvider content;
	private TableViewer viewer;
//	private Action action1;
//	private Action action2;
//	private Action doubleClickAction;

	private Label lastBuild;

	private SimpleDateFormat sdf;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		private final List<String> msgs = new ArrayList<String>();
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return msgs.toArray();
		}
		public void add(String elt) {
			msgs.add(elt);
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public HexagonView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		sdf = new SimpleDateFormat("HHmmss.SSS");
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);
		lastBuild = new Label(parent, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		content = new ViewContentProvider();
		viewer.setContentProvider(content);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		/*
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		*/
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("Resource changed " + event);
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(0);
		Set<String> scanned = new TreeSet<String>();
		for (IProject p : projects) {
			try {
//				IBuildConfiguration bc = p.getActiveBuildConfig();
//				System.out.println(bc.getClass());
				IJavaProject jp = JavaCore.create(p);
				if (jp != null) {
					scanUnder(scanned, p, jp.getOutputLocation());
					for (IClasspathEntry e : jp.getRawClasspath()) {
						System.out.println(e);
						scanUnder(scanned, p, e.getOutputLocation());
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lastBuild.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				lastBuild.setText(sdf.format(new Date()));
				viewer.refresh();
			}
		});
	}

	private void scanUnder(Set<String> scanned, IProject p, IPath op) throws CoreException {
		if (op == null)
			return;
		File root = p.getWorkspace().getRoot().getFolder(op).getRawLocation().toFile();
		scanAll(scanned, root, root);
	}

	private void scanAll(Set<String> scanned, File root, File under) throws CoreException {
		if (under == null)
			return;
		String path = under.getPath();
		if (scanned.contains(path))
			return;
		scanned.add(path);
		System.out.println("Scanning " + under);
		if (under.isDirectory()) {
			for (File m : under.listFiles())
				scanAll(scanned, root, m);
		} else if (under.isFile() && under.getName().endsWith(".class")) {
			content.add(path.substring(root.getPath().length()+1));
		} else
			System.out.println("Cannot handle " + under.getClass());
	}

	/*
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				HexagonView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Hexagon",
			message);
	}
	*/
}
