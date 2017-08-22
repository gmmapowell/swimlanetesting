package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gmmapowell.swimlane.eclipse.interfaces.AcceptanceData;
import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.UtilityData;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewComponentFactory;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;

public class SwimlaneView implements ViewLayout {
	private final ViewComponentFactory factory;
	private final Composite view;

	public SwimlaneView(Composite parent, ViewComponentFactory factory) {
		this.factory = factory;
		view = new Composite(parent, SWT.NONE);
		view.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SwimlaneLayout layout = new SwimlaneLayout();
		view.setLayout(layout);
	}

	public Control getTop() {
		return view;
	}

	@Override
	public void addHexagon(int pos, HexData hi) {
		HexagonBackground hc = factory.hexagon(view, pos);
		BarControl bc = factory.bar(view, "business." + pos);
		bc.getCanvas().setLayoutData(new BusinessBarLayout(hc, bc.getCanvas()));
		hi.addBusinessLogicListener(bc);
		view.layout();
	}

	@Override
	public void addAcceptance(int[] hexes, AcceptanceData ad) {
		String mask = maskString(hexes);
		BarControl bc = factory.bar(view, "acceptance." + mask);
		bc.getCanvas().setLayoutData(new AcceptanceBarLayout(mask, bc.getCanvas()));
		ad.addTestListener(bc);
		view.layout();
	}
	
	private String maskString(int[] hexes) {
		StringBuilder sb = new StringBuilder();
		for (int i : hexes) {
			sb.append(i);
		}
		return sb.toString();
	}

	@Override
	public void addHexagonPort(int pos, PortLocation loc, PortData port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAdapter(int hex, PortLocation ploc, int aloc, AdapterData adapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUtility(UtilityData ad) {
		BarControl bc = factory.bar(view, "utility");
		bc.getCanvas().setLayoutData(new UtilityBarLayout(bc.getCanvas()));
		ad.addTestListener(bc);
		view.layout();
	}

	// TODO: break a lot of this stuff off into a factory
	// couple tightly with the SwimlaneLayout
	// try and make things get the main model to hold onto them
	// then worry about updates later ...
//	private void update(HexagonDataModel model) {
//		view.getDisplay().syncExec(new Runnable() {
//			@Override
//			public void run() {
//				for (BarData accModel : model.getAcceptanceTests()) {
//					String accId = "swimlane." + accModel.getId();
//					BarControl bc = findBar(view, accId);
//					if (bc == null)
//						createBar(model, accModel, "accbar", accId);
//					else
//						bc.barChanged(accModel);
//				}
//				int hexn = 1;
//				for (HexData hexModel : model.getHexagons()) {
//					String hexId = "swimlane." + hexModel.getId();
//					HexagonControl hc = findHexagon(view, hexId + ".bg");
//					if (hc == null)
//						hc = createHexagon(hexModel, hexn, hexId);
//					hc.barChanged(hexModel.getBar());
//					hexn++;
//				}
//				BarData bd = model.getUtilityBar();
//				if (bd != null) {
//					String id = "swimlane." + bd.getId();
//					BarControl bc = findBar(view, id);
//					if (bc == null)
//						bc = createBar(model, bd, "utebar", id);
//					else
//						bc.barChanged(bd);
//				}
//			}
//		});
//	}

//	protected BarControl createBar(HexagonDataModel model, BarData accModel, String barType, String accId) {
//		BarControl bc = new BarControl(view, accModel, barType, accId);
//
//		// By default, the bar will have been added at "the end".  If that is the wrong place, we need to consider moving it up
//		// In particular, it should go before any keys that are "acceptance.N" where N is less than our N,
//		// and certainly before any hexagons
//		// TODO: this code is not currently general enough to handle more than just acceptance bars
//		// TODO: we should probably refactor the ordering out into a separate piece of logic
//		for (Control c : view.getChildren()) {
//			String type = (String) c.getData("com.gmmapowell.swimlane.type");
//			String okey = (String) c.getData("org.eclipse.swtbot.widget.key");
//			// This test assumes that they collate in string order, which would not be true if we are using unpadded integers
//			if (type != null && type.equals("hexbg")) { // move it before a bg
//				bc.getCanvas().moveAbove(c);
//				break;
//			} else if (okey != null && okey.startsWith("swimlane.acceptance.") && okey.compareTo(accId) < 0) {
//				bc.getCanvas().moveAbove(c);
//				break;
//			}
//		}
//		view.layout();
//		return bc;
//	}

//	private BarControl findBar(Control c, String which) {
//		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
//			return (BarControl) c.getData("com.gmmapowell.swimlane.bar");
//		if (c instanceof Composite) {
//			for (Control ch : ((Composite)c).getChildren()) {
//				BarControl r = findBar(ch, which);
//				if (r != null)
//					return r;
//			}
//				
//		}
//		return null;
//	}

//	private HexagonControl findHexagon(Control c, String which) {
//		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
//			return (HexagonControl) c.getData("com.gmmapowell.swimlane.hex");
//		if (c instanceof Composite) {
//			for (Control ch : ((Composite)c).getChildren()) {
//				HexagonControl r = findHexagon(ch, which);
//				if (r != null)
//					return r;
//			}
//				
//		}
//		return null;
//	}
}
