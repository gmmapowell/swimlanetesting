package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;

public class HexView implements HexagonModelListener, AccumulatorListener {
	private final Composite view;
	private final ModelDispatcher dispatcher;
	private HexagonDataModel model;
	private Accumulator accumulator;

	public HexView(Composite parent, ModelDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		view = new Composite(parent, SWT.NONE);
		view.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dispatcher.addHexagonModelListener(this);
		dispatcher.addAccumulator(this);
	}

	public Control getTop() {
		return view;
	}

	public HexagonDataModel getModel() {
		return model;
	}
	
	public void setModel(HexagonDataModel model) {
		this.model = model;
		view.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				update(model);
			}
		});
	}

	private void update(HexagonDataModel model) {
		HexagonLayout layout = new HexagonLayout();
		view.setLayout(layout);
		for (BarData accModel : model.getAcceptanceTests()) {
			String accId = "hexagons." + accModel.getId();
			if (findBar(view, accId) == null)
				createBar(model, accModel, "accbar", accId);
		}
		int hexn = 1;
		for (HexData hexModel : model.getHexagons()) {
			String hexId = "hexagons." + hexModel.getId();
			HexagonControl hc = findHexagon(view, hexId + ".bg");
			if (hc == null)
				hc = createHexagon(hexModel, hexn, hexId);
			hc.setBarVisibility(hexModel.getBar() != null && hexModel.getBar().getTotal() > 0);
			hexn++;
		}
		BarData bd = model.getUtilityBar();
		if (bd != null) {
			String id = "hexagons." + bd.getId();
			BarControl bc = findBar(view, id);
			if (bc == null)
				bc = createBar(model, bd, "utebar", id);
			else
				bc.barChanged(bd);
		}
	}

	protected BarControl createBar(HexagonDataModel model, BarData accModel, String barType, String accId) {
		BarControl bc = new BarControl(dispatcher, view, accModel, barType, accId);

		// By default, the bar will have been added at "the end".  If that is the wrong place, we need to consider moving it up
		// In particular, it should go before any keys that are "acceptance.N" where N is less than our N,
		// and certainly before any hexagons
		// TODO: this code is not currently general enough to handle more than just acceptance bars
		// TODO: we should probably refactor the ordering out into a separate piece of logic
		for (Control c : view.getChildren()) {
			String type = (String) c.getData("com.gmmapowell.swimlane.type");
			String okey = (String) c.getData("org.eclipse.swtbot.widget.key");
			// This test assumes that they collate in string order, which would not be true if we are using unpadded integers
			if (type != null && type.equals("hexbg")) { // move it before a bg
				bc.getCanvas().moveAbove(c);
				break;
			} else if (okey != null && okey.startsWith("hexagons.acceptance.") && okey.compareTo(accId) < 0) {
				bc.getCanvas().moveAbove(c);
				break;
			}
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

	protected HexagonControl createHexagon(HexData hexModel, int hexn, String hexId) {
		HexagonControl hex = new HexagonControl(dispatcher, view, hexn, hexId, hexModel.getBar(), hexModel.getPorts());

		// Move the hex bar above any backgrounds
		// The background is automatically added at the end
		for (Control c : view.getChildren()) {
			String type = (String) c.getData("com.gmmapowell.swimlane.type");
			if (type != null && type.equals("hexbg")) {
				hex.getBar().moveAbove(c);
				for (PortControl pc : hex.getPorts())
					pc.getCanvas().moveAbove(c);
				break;
			}
		}
		view.layout();
		return hex;
	}
	
	@Override
	public void setModel(Accumulator model) {
		accumulator = model;
		
	}

	public Accumulator getAccumulator() {
		return accumulator;
	}
}
