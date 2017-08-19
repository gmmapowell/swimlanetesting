package com.gmmapowell.swimlane.tests.accumulator;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class AcceptanceAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	AnalysisAccumulator acc = new HexagonAccumulator();
	Solution solution = context.mock(Solution.class);
	ErrorMessageListener errors = context.mock(ErrorMessageListener.class);
	Sequence seq = context.sequence("solution");
	TestGroup grp = new TestGroup("Project", null);
	Date bcd = new Date();
	Class<?> hexClass1 = Integer.class;
	Class<?> hexClass2 = List.class;
	Class<?> hexClass3 = Set.class;
	Class<?> hexClass4 = Array.class;
	HexInfoMatcher hmd = HexInfoMatcher.called(null);
	HexInfoMatcher hm1 = HexInfoMatcher.called(hexClass1);
	HexInfoMatcher hm2 = HexInfoMatcher.called(hexClass2);
	HexInfoMatcher hm3 = HexInfoMatcher.called(hexClass3);
	HexInfoMatcher hm4 = HexInfoMatcher.called(hexClass4);

	@Before
	public void setup() {
		context.checking(new Expectations() {{
			oneOf(errors).clear();
		}});
		((DataCentral)acc).setSolution(solution);
		((DataCentral)acc).addErrorMessageListener(errors);
	}
	
	@Test
	public void testNoTestsMeansNoHexes() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testOneUnboundAcceptanceGivesOneHex() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hmd)); inSequence(seq);
			oneOf(solution).portsDone(with(hmd)); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole());
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testOneAcceptanceWithTwoHexesGivesTwoHexesAndNoErrors() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesWithTwoHexesGivesTwoHexesButComplainsAboutNoTotalOrdering() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(errors).error("there is no ordering between java.lang.Integer and java.util.List");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoHexesInDifferentOrdersGivesTwoHexesButComplainsAboutInconsistentOrdering() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(errors).error("ordering between java.lang.Integer and java.util.List is inconsistent");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass1));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingOfThreeHexes() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testTwoAcceptancesEachWithTwoOrderedHexesIsNotTotal() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
			oneOf(errors).error("there is no ordering between java.util.List and java.util.Set");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass1, hexClass3));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOrderedHexesIsNotConsistent() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
			oneOf(errors).error("there is a cycle between java.lang.Integer and java.util.Set");
			oneOf(errors).error("there is a cycle between java.lang.Integer and java.util.List");
			oneOf(errors).error("there is a cycle between java.util.List and java.util.Set");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3));
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass3, hexClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingAfterTwoPasses() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hex(with(hm4)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm4)); inSequence(seq);
			oneOf(solution).portsDone(with(hm4)); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3));
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass3, hexClass4));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeConsistentWithOneCoveringAllThree() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2, hexClass3));
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass1, hexClass2));
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeInonsistentWithOneCoveringAllThreeIfInADifferentOrder() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm3)); inSequence(seq);
			oneOf(solution).portsDone(with(hm3)); inSequence(seq);
			oneOf(errors).error("ordering between java.lang.Integer and java.util.List is inconsistent");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2, hexClass3));
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass2, hexClass1));
		acc.analysisComplete(bcd);
	}
}
