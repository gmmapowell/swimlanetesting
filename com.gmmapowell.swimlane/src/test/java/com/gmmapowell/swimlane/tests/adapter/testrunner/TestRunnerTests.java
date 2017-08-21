package com.gmmapowell.swimlane.tests.adapter.testrunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.IViewPart;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.handlers.RunAllTestsHandler;
import com.gmmapowell.swimlane.eclipse.interfaces.CommandDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestRunnerTests extends TestBase {

	interface HVP extends CommandDispatcher, IViewPart {
	}
	
	@Test
	public void testThatTheDateIsUpdatedWhenWePushTheRunButton() throws ExecutionException {
		RunAllTestsHandler action = new RunAllTestsHandler();
		List<TestGroup> tcs = new ArrayList<>();
		TestGroup tg = new TestGroup("Project", Arrays.asList(new File("a"), new File("b"), new File("c")));
		tcs.add(tg);
		tg.addTest("com.foo");
		IEvaluationContext applicationContext = context.mock(IEvaluationContext.class);
		CommandDispatcher hv = context.mock(HVP.class);
		context.checking(new Expectations() {{
			allowing(applicationContext).getVariable("activePart"); will(returnValue(hv));
			allowing(hv).runAllTests();
		}});
		action.execute(new ExecutionEvent(null, new HashMap<Object, Object>(), null, applicationContext));
	}
}
