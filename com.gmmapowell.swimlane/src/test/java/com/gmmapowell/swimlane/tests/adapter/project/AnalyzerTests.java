package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.tests.hamcrest.ClassMatcher;

public class AnalyzerTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	private HexagonTestAnalyzer analyzer;
	private Accumulator accumulator;
	private URLClassLoader cl;
	private TestGroup grp = new TestGroup(null);

	@Before
	public void setup() throws MalformedURLException {
		accumulator = context.mock(Accumulator.class);
		File anns = new File("../swimlane-annotations/bin/classes/");
		File root = new File("../swimlane-annotations/bin/testclasses");
		cl = new URLClassLoader(new URL[] { root.toURI().toURL(), anns.toURI().toURL() });
		analyzer = new HexagonTestAnalyzer(grp, cl, accumulator);
	}

	@Test
	public void testWeCanDetectTheAcceptanceAnnotationOnATestClass() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptance1";
		List<Class<?>> al = new ArrayList<Class<?>>();
		context.checking(new Expectations() {{
			oneOf(accumulator).acceptance(with(grp), with(ClassMatcher.named(clzName)), with(al));
		}});
		analyzer.consider(clzName);
	}

	@Test
	public void testWeCanDetectTheHexagonsInAnAcceptanceAnnotation() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptanceWithHexes";
		List<Class<?>> al = new ArrayList<Class<?>>();
		al.add(cls("com.gmmapowell.swimlane.samples.Hexagon1"));
		al.add(cls("com.gmmapowell.swimlane.samples.Hexagon2"));
		context.checking(new Expectations() {{
			oneOf(accumulator).acceptance(with(grp), with(ClassMatcher.named(clzName)), with(al));
		}});
		analyzer.consider(clzName);
	}

	@Test
	public void testWeAreNotifiedIfTheClassCannotBeFound() throws Exception {
		String clzName = "unknown";
		context.checking(new Expectations() {{
			oneOf(accumulator).error("No such class: unknown");
		}});
		analyzer.consider(clzName);
	}
	
	private Class<?> cls(String string) throws Exception {
		return Class.forName(string, false, cl);
	}
}
