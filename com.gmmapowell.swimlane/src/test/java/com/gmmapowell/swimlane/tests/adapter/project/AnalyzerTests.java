package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.tests.hamcrest.ClassMatcher;

public class AnalyzerTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void testWeCanDetectTheAcceptanceAnnotationOnATestClass() throws Exception {
		Accumulator accumulator = context.mock(Accumulator.class);
		File anns = new File("../swimlane-annotations/bin/classes/");
		File root = new File("../swimlane-annotations/bin/testclasses");
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptance1";
		URLClassLoader cl = new URLClassLoader(new URL[] { root.toURI().toURL(), anns.toURI().toURL() });
		HexagonTestAnalyzer analyzer = new HexagonTestAnalyzer(cl, accumulator);
		context.checking(new Expectations() {{
			oneOf(accumulator).acceptance(with(ClassMatcher.named(clzName)));
		}});
		analyzer.consider(clzName);
	}

}
