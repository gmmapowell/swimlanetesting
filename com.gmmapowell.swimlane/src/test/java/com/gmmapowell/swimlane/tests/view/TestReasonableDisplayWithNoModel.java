package com.gmmapowell.swimlane.tests.view;

import org.junit.Test;

// As the name implies, we want to test what happens to the view part display
// when there is no data model in place
public class TestReasonableDisplayWithNoModel extends AViewTest {

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		assertControls(shell, "hexagons.lastBuild");
	}
}
