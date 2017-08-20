package com.gmmapowell.swimlane.tests.view.hex;

import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.views.SwimlaneView;

public abstract class BaseHexViewTest extends BaseViewTest {
	public SwimlaneView hv;

	@Before
	public void setup() throws Exception {
		super.create();
		hv = new SwimlaneView(shell);
		super.complete();
	}
}
