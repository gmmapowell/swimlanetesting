package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
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
	private ErrorAccumulator eh = context.mock(ErrorAccumulator.class);
	private AnalysisAccumulator accumulator;
	private URLClassLoader cl;
	private GroupOfTests grp;
	private String hex1, hex2;
	private String adapter1, port1;
	private DataCentral hub;
	List<String> tests = new ArrayList<>();

	@Before
	public void setup() throws Exception {
		grp = context.mock(GroupOfTests.class);
		hub = context.mock(DataCentral.class);
		accumulator = context.mock(AnalysisAccumulator.class);
		Date bcd = new Date();
		context.checking(new Expectations() {{
			oneOf(hub).startAnalysis(bcd); will(returnValue(accumulator));
		}});
		File anns = new File("../swimlane-annotations/bin/classes/");
		File root = new File("../swimlane-annotations/bin/testclasses");
		cl = new URLClassLoader(new URL[] { root.toURI().toURL(), anns.toURI().toURL() });
		analyzer = new HexagonTestAnalyzer(eh, hub);
		hex1 = "com.gmmapowell.swimlane.samples.Hexagon1";
		hex2 = "com.gmmapowell.swimlane.samples.Hexagon2";
		adapter1 = "com.gmmapowell.swimlane.samples.Hex1Port1Adapter1";
		port1 = "com.gmmapowell.swimlane.samples.Hex1Port1";
		analyzer.startAnalysis(bcd);
	}

	@Test
	public void testWeCanDetectTheAcceptanceAnnotationOnATestClass() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptance1";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AcceptanceRoleMatcher.withHexes()), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectTheHexagonsInAnAcceptanceAnnotation() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAcceptanceWithHexes";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AcceptanceRoleMatcher.withHexes(hex1, hex2)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenABusinessTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleBusiness";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(BusinessRoleMatcher.logic(null)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenABusinessTestIsIndicatedForASpecificHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleBusinessWithHex";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(BusinessRoleMatcher.logic(hex1)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter1";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicatedWithAHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter2";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(hex1).adapter(adapter1)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestIsIndicatedWithAPort() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter3";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1).port(port1)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAnAdapterTestSpecifiesALocationForItsPortRelativeToTheHexagon() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleAdapter4";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(AdapterRoleMatcher.hex(null).adapter(adapter1).location(PortLocation.NORTHWEST)), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectWhenAUtilityTestIsIndicated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.SampleUtility";
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(UtilityRoleMatcher.matcher()), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeCanDetectTestsThatHaventBeenAnnotated() throws Exception {
		String clzName = "com.gmmapowell.swimlane.samples.UnlabelledTest";
		tests.add("testSomething");
		context.checking(new Expectations() {{
			oneOf(accumulator).haveTestClass(with(grp), with(clzName), with(UnlabelledRoleMatcher.matcher()), with(tests));
		}});
		analyzer.consider(grp, cl, clzName);
	}

	@Test
	public void testWeAreNotifiedIfTheClassCannotBeFound() throws Exception {
		String clzName = "unknown";
		context.checking(new Expectations() {{
			oneOf(eh).error("No such class: unknown");
		}});
		analyzer.consider(grp, cl, clzName);
	}
}
