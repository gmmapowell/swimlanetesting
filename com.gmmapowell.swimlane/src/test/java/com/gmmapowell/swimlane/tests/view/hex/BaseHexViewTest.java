package com.gmmapowell.swimlane.tests.view.hex;

import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.views.HexView;

public abstract class BaseHexViewTest extends BaseViewTest {
	public HexView hv;

	@Before
	public void setup() throws Exception {
		super.create();
		hv = new HexView(shell);
		super.complete();
	}
}
