package com.gmmapowell.swimlane.tests.view.errors;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Table;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;

public class TestTwoSimpleMessages extends BaseViewTest {

	@Test
	public void testThatTheErrorsPaneIsATableWithTwoRows() throws InterruptedException {
		specifyModel();
		Table table = waitForControl(shell, "hexagons.errors");
		assertEquals(2, table.getItemCount());
	}

	@Test
	public void testThatSendingTheModelTwiceDoesNotDuplicateRows() throws InterruptedException {
		HexagonDataModel m = defineModel();
		pushModel(m);
		pushModel(m);
		Table table = waitForControl(shell, "hexagons.errors");
		assertEquals(2, table.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		Set<String> errors = new TreeSet<String>();
		errors.add("hello");
		errors.add("goodbye");
		context.checking(new Expectations() {{
			allowing(testModel).getErrors(); will(returnValue(errors));
		}});
		return testModel;
	}

}
