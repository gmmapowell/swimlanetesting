package com.gmmapowell.swimlane.tests.view.hex;

import org.junit.Test;

// As the name implies, we want to test what happens to the view part display
// when there is no data model in place
public class TestReasonableDisplayWithNoModel extends BaseHexViewTest {

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		assertControls(shell);
	}
}
