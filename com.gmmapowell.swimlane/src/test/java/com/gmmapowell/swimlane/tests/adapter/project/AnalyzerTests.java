package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.testsupport.matchers.AcceptanceRoleMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.AdapterRoleMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.BusinessRoleMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.UnlabelledRoleMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.UtilityRoleMatcher;

public class AnalyzerTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	private HexagonTestAnalyzer analyzer;
	private AnalysisAccumulator accumulator;
	private URLClassLoader cl;
	private GroupOfTests grp;
	private String hex1, hex2;
	private String adapter1, port1;

	@Before
	public void setup() throws Exception {
		grp = context.mock(GroupOfTests.class);
		accumulator = context.mock(AnalysisAccumulator.class);
		File anns = new File("../swimlane-annotations/bin/classes/");
		File root = new File("../swimlane-annotations/bin/testclasses");
		cl = new URLClassLoader(new URL[] { root.toURI().toURL(), anns.toURI().toURL() });
		analyzer = new HexagonTestAnalyzer(accumulator);
		hex1 = "com.gmmapowell.swimlane.samples.Hexagon1";
		hex2 = "com.gmmapowell.swimlane.samples.Hexagon2";
		adapter1 = "com.gmmapowell.swimlane.samples.Hex1Port1Adapter1";
		port1 = "com.gmmapowell.swimlane.samples.Hex1Port1";
	}

	@Test
	public void testWeCanDetectTheAcceptanceAnnotationOnATestClass() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptance1";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AcceptanceRoleMatcher.withHexes()));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectTheHexagonsInAnAcceptanceAnnotation() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptanceWithHexes";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AcceptanceRoleMatcher.withHexes(hex1, hex2)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenABusinessTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleBusiness";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(BusinessRoleMatcher.logic(null)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenABusinessTestIsIndicatedForASpecificHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleBusinessWithHex";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(BusinessRoleMatcher.logic(hex1)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter1";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicatedWithAHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter2";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(hex1).adapter(adapter1)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicatedWithAPort() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter3";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1).port(port1)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestSpecifiesALocationForItsPortRelativeToTheHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter4";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1).location(PortLocation.NORTHWEST)));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAUtilityTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleUtility";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(UtilityRoleMatcher.matcher()));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectTestsThatHaventBeenAnnotated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.UnlabelledTest";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(UnlabelledRoleMatcher.matcher()));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeAreNotifiedIfTheClassCannotBeFound() throws Exception {
		String clzName = "unknown";
		context.checking(new Expectations() {{
			oneOf(accumulator).error("No such class: unknown");
		}});
		analyzer.consider(grp, cl, clzName);
	}
}
