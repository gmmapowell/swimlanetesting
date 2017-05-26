package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class HexView implements HexagonModelListener {
	private final Composite view;
	private final ModelDispatcher dispatcher;

	public HexView(Composite parent, ModelDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		view = new Composite(parent, SWT.NONE);
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dispatcher.addHexagonModelListener(this);
	}

	public void setModel(HexagonDataModel model) {
		view.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				update(model);
			}
		});
	}

	private void update(HexagonDataModel model) {
		HexagonLayout layout = new HexagonLayout(model);
		view.setLayout(layout);
		for (BarData accModel : model.getAcceptanceTests()) {
			String accId = "hexagons." + accModel.getId();
			if (findBar(view, accId) == null)
				createBar(model, accModel, accId);
		}
		for (HexData hexModel : model.getHexagons()) {
			String hexId = "hexagons." + hexModel.getId();
			if (findHexagon(view, hexId) == null)
				createHexagon(hexModel, hexId);
		}
	}

	protected BarControl createBar(HexagonDataModel model, BarData accModel, String accId) {
		BarControl bc = new BarControl(dispatcher, view, accModel, "accbar", accId);

		// By default, the bar will have been added at "the end".  If that is the wrong place, we need to consider moving it up
		// In particular, it should go before any keys that are "acceptance.N" where N is less than our N,
		// and certainly before any hexagons
		// TODO: this code is not currently general enough to handle more than just acceptance bars
		// TODO: we should probably refactor the ordering out into a separate piece of logic
		for (Control c : view.getChildren()) {
			String okey = (String) c.getData("org.eclipse.swtbot.widget.key");
			// This test assumes that they collate in string order, which would not be true if we are using unpadded integers 
			if (okey != null && okey.startsWith("hexagons.acceptance.") && okey.compareTo(accId) < 0)
				bc.getCanvas().moveAbove(c);
		}
		view.layout();
		return bc;
	}

	private BarControl findBar(Control c, String which) {
		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
			return (BarControl) c.getData("com.gmmapowell.swimlane.bar");
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				BarControl r = findBar(ch, which);
				if (r != null)
					return r;
			}
				
		}
		return null;
	}

	private HexagonControl findHexagon(Control c, String which) {
		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
			return (HexagonControl) c.getData("com.gmmapowell.swimlane.hex");
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				HexagonControl r = findHexagon(ch, which);
				if (r != null)
					return r;
			}
				
		}
		return null;
	}

	protected HexagonControl createHexagon(HexData hexModel, String hexId) {
		HexagonControl hex = new HexagonControl(dispatcher, view, hexModel.getBar(), hexId);

		// Move the background up above any bars
		for (Control c : view.getChildren()) {
			String type = (String) c.getData("com.gmmapowell.swimlane.type");
			if (type == null || !type.equals("hexbg")) {
				hex.getBackground().moveAbove(c);
				hex.getBar().moveAbove(hex.getBackground());
				break;
			}
		}
		view.layout();
		return hex;
	}

}
