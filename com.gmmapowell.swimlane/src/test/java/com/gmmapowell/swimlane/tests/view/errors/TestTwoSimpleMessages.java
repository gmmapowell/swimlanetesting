package com.gmmapowell.swimlane.tests.view.errors;

public class TestTwoSimpleMessages extends BaseViewTest {
/*
	@Test
	public void testThatTheErrorsPaneIsATableWithTwoRows() throws InterruptedException {
		specifyModel();
		Table table = waitForControl(shell, "swimlane.errors");
		assertEquals(2, table.getItemCount());
	}

	@Test
	public void testThatSendingTheModelTwiceDoesNotDuplicateRows() throws InterruptedException {
		HexagonDataModel m = defineModel();
		pushModel();
		pushModel();
		Table table = waitForControl(shell, "swimlane.errors");
		assertEquals(2, table.getItemCount());
	}

	protected void specifyModel() throws InterruptedException {
		pushModel();
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
*/
}
