package com.gmmapowell.swimlane.tests.adapter.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.views.RunAllTestsAction;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestRunnerTests extends TestBase {

	@Test
	public void testThatTheDateIsUpdatedWhenWePushTheRunButton() {
		ModelDispatcher md = context.mock(ModelDispatcher.class);
		RunAllTestsAction action = new RunAllTestsAction(md);
		HexagonAccumulator hex = new HexagonAccumulator();
		action.setModel(hex);
		Date nd = new Date();
		context.checking(new Expectations() {{
			oneOf(md).setModel(hex);
		}});
		action.run();
		assertNotNull("The test completion time was not set", hex.getTestCompleteTime());
		assertFalse("The test completion time was not not after the current time", nd.after(hex.getTestCompleteTime()));
	}

}
