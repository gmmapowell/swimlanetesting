package com.gmmapowell.swimlane.eclipse.views;

import java.text.SimpleDateFormat;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.project.BuildListener;


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

public class HexagonViewPart extends ViewPart implements HexagonModelListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.gmmapowell.swimlane.views.HexagonView";

	private final SimpleDateFormat sdf;

	private Label lastBuild;
	private Composite parent;

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
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new BuildListener(this), IResourceChangeEvent.POST_BUILD);
		createControls(parent);
	}

	public void createControls(Composite parent) {
		this.parent = parent;
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lastBuild = new Label(parent, SWT.NONE);
		lastBuild.setData("org.eclipse.swtbot.widget.key", "hexagons.lastBuild");
		lastBuild.setText("none");
	}

	public void setFocus() {
	}

	public void setModel(HexagonDataModel model) {
		lastBuild.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				parent.setLayout(new GridLayout(model.getHexCount(), false));
				lastBuild.setText(sdf.format(model.getBuildTime()));
				for (BarData accModel : model.getAcceptanceTests()) {
					String accId = "hexagons." + accModel.getId();
					if (findBar(parent, accId) == null) {
						Canvas acceptance = new Canvas(parent, SWT.NONE);
						acceptance.setData("org.eclipse.swtbot.widget.key", accId);
						GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
						gd.heightHint = 6;
						gd.horizontalSpan = model.getHexCount();
						acceptance.setLayoutData(gd);
						acceptance.addPaintListener(new PaintListener() {
							
							@Override
							public void paintControl(PaintEvent e) {
								int total = accModel.getTotal();
								int compl = accModel.getComplete();
								int[] marks = accModel.getMarks();
								Point size = acceptance.getSize();
								int segwidth = size.x/model.getHexCount();
								int markedx = bitcount(marks)*segwidth;
								int barx = markedx*compl/total;
								GC gc = new GC(acceptance);
								for (int i=0;i<marks.length;i++) {
									int from = size.x*i/model.getHexCount();
									int to = size.x*(i+1)/model.getHexCount();
									if (marks[i] == 1) {
										if (barx > from) {
											Color barColor = parent.getDisplay().getSystemColor(getColor(accModel.getStatus()));
											gc.setBackground(barColor);
											gc.fillRectangle(from, 0, Math.min(barx-from, to-from), size.y);
										}
										if (barx < to) {
											int left = Math.max(barx, from);
											Color grey = parent.getDisplay().getSystemColor(SWT.COLOR_GRAY);
											gc.setBackground(grey);
											gc.fillRectangle(left, 0, Math.min(markedx-left, to-from), size.y);
										}
									} else {
										barx += segwidth;
										markedx += segwidth;
									}
								}
								gc.dispose();
							}
	
							private int bitcount(int[] marks) {
								int sum = 0;
								for (int i=0;i<marks.length;i++)
									sum+=marks[i];
								return sum;
							}

							private int getColor(Status status) {
								switch (status) {
								case NONE:
									return SWT.COLOR_GRAY;
								case OK:
									return SWT.COLOR_GREEN;
								case FAILURES:
									return SWT.COLOR_RED;
								case SKIPPED:
									return SWT.COLOR_YELLOW;
								}
								throw new RuntimeException("Cannot handle status " + status);
							}
						});

						// We add controls at "the end".  If that is the wrong place, weneed to consider moving it up
						// In particular, it should go before any keys that are "acceptance.N" where N is less than our N,
						// and certainly before any hexagons
						for (Control c : parent.getChildren()) {
							String okey = (String) c.getData("org.eclipse.swtbot.widget.key");
							// This test assumes that they collate in string order, which would not be true if we are using unpadded integers 
							if (okey != null && okey.startsWith("hexagons.acceptance.") && okey.compareTo(accId) < 0)
								acceptance.moveAbove(c);
						}
						parent.layout();
					}
				}
			}

		});
	}

	@SuppressWarnings("unchecked")
	private <T extends Control> T findBar(Control c, String which) {
		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
			return (T)c;
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				Control r = findBar(ch, which);
				if (r != null)
					return (T) r;
			}
				
		}
		return null;
	}
}
