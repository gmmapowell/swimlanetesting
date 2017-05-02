package com.gmmapowell.swimlane.tests.adapter.testrunner;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.testrunner.TestResultAnalyzer;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestAnalyzerTests extends TestBase {

	@Test
	public void testThatATESTCCommandMapsToTestCount() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		context.checking(new Expectations() {{
			oneOf(trr).testCount(2);
		}});
		tra.push("%TESTC  2 v2");
	}

	@Test
	public void testThatV3IsNotSupported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		context.checking(new Expectations() {{
			oneOf(trr).testError("Cannot handle protocol v3");
		}});
		tra.push("%TESTC  2 v3");
	}

}
