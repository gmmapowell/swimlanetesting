package com.gmmapowell.swimlane.tests.view.hex;

import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.interfaces.ShowErrorsPane;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewComponentFactory;
import com.gmmapowell.swimlane.eclipse.views.SwimlaneView;
import com.gmmapowell.swimlane.eclipse.views.SwimlaneViewComponentFactory;

public abstract class BaseHexViewTest extends BaseViewTest {
	public SwimlaneView swimlane;
	protected ShowErrorsPane sep = context.mock(ShowErrorsPane.class);

	@Before
	public void setup() throws Exception {
		super.create();
		ViewComponentFactory factory = new SwimlaneViewComponentFactory();
		swimlane = new SwimlaneView(shell, factory, sep);
		super.complete();
	}
}
