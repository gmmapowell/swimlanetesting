package com.gmmapowell.swimlane.tests.view.errors;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Table;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class MinimalErrorsTest extends BaseViewTest {

	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		specifyModel();
		assertControls(shell, "hexagons.errors");
	}

	@Test
	public void testThatTheErrorsPaneIsATableWithTwoColumns() throws InterruptedException {
		specifyModel();
		Table table = waitForControl(shell, "hexagons.errors");
		assertEquals(2, table.getColumnCount());
		assertEquals("Sev", table.getColumn(0).getText());
		assertEquals("Message", table.getColumn(1).getText());
		assertEquals(0, table.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		return testModel;
	}

}
