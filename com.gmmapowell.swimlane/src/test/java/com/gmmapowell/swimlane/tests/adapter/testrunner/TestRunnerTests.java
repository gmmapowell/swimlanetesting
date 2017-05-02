package com.gmmapowell.swimlane.tests.adapter.testrunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.views.RunAllTestsAction;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestRunnerTests extends TestBase {

	interface HexAcc extends TestResultReporter, Accumulator, HexagonDataModel {
	}
	
	@Test
	public void testThatTheDateIsUpdatedWhenWePushTheRunButton() {
		ModelDispatcher md = context.mock(ModelDispatcher.class);
		TestRunner tr = context.mock(TestRunner.class);
		RunAllTestsAction action = new RunAllTestsAction(tr, md);
		HexAcc hex = context.mock(HexAcc.class);
		action.setModel(hex);
		String cp = "a:b:c";
		List<TestGroup> tcs = new ArrayList<>();
		TestGroup tg = new TestGroup(cp);
		tcs.add(tg);
		tg.addTest("com.foo");
		context.checking(new Expectations() {{
			oneOf(hex).getAllTestClasses(); will(returnValue(tcs));
			oneOf(tr).runClass(hex, cp, "com.foo");
			oneOf(md).setModel(hex);
			oneOf(hex).testsCompleted(with(any(Date.class)));
		}});
		action.run();
	}

}
