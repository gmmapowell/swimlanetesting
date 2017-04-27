package com.gmmapowell.swimlane.tests.adapter.project;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.views.RunAllTestsAction;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestRunnerTests extends TestBase {

	@Test
	public void testThatTheDateIsUpdatedWhenWePushTheRunButton() {
		RunAllTestsAction action = new RunAllTestsAction();
		HexagonAccumulator hex = new HexagonAccumulator();
		action.setModel(hex);
		action.run();
		assertEquals(new Date(), hex.getTestCompleteTime());
	}

}
