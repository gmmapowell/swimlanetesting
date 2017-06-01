package com.gmmapowell.swimlane.tests.view.errors;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class MinimalErrorsTest extends BaseViewTest {

	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		specifyModel();
		assertControls(shell, "hexagons.errors");
	}
	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		return testModel;
	}

}
