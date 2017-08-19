package com.gmmapowell.swimlane.tests.accumulator;

import java.util.Date;

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
	HexInfoMatcher hmd = HexInfoMatcher.called(null);

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
	
	/*
	@Test
	public void testOneAcceptanceWithTwoHexesGivesTwoHexesAndNoErrors() {
		acc.acceptance(grp, String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.11", acceptanceTests.get(0).getId());
	}
	
	@Test
	public void testTwoAcceptancesWithTwoHexesGivesTwoHexesButComplainsAboutNoTotalOrdering() {
		acc.acceptance(grp, Integer.class, Arrays.asList(Integer.class));
		acc.acceptance(grp, String.class, Arrays.asList(String.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("there is a cycle between java.lang.Integer and java.lang.String", errs[0]);
		assertEquals("there is no ordering between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.10", acceptanceTests.get(0).getId());
		assertEquals("acceptance.01", acceptanceTests.get(1).getId());
		assertEquals(2, grp.getClasses().length);
		assertEquals(Integer.class.getName(), grp.getClasses()[0]);
		assertEquals(String.class.getName(), grp.getClasses()[1]);
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoHexesInDifferentOrdersGivesTwoHexesButComplainsAboutInconsistentOrdering() {
		acc.acceptance(grp, String.class, Arrays.asList(Integer.class, String.class));
		acc.acceptance(grp, String.class, Arrays.asList(String.class, Integer.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("ordering between java.lang.Integer and java.lang.String is inconsistent", errs[0]);
		assertEquals("there is a cycle between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.11", acceptanceTests.get(0).getId());
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingOfThreeHexes() {
		acc.acceptance(grp, Long.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(grp, Float.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		BarData acc0 = acceptanceTests.get(0);
		assertEquals("acceptance.110", acc0.getId());
		assertEquals(1, acc0.classesUnderTest().size());
		assertEquals(Long.class.getName(), acc0.classesUnderTest().get(0));
		BarData acc1 = acceptanceTests.get(1);
		assertEquals("acceptance.011", acc1.getId());
		assertEquals(1, acc1.classesUnderTest().size());
		assertEquals(Float.class.getName(), acc1.classesUnderTest().get(0));
	}

	@Test
	public void testTwoAcceptancesEachWithTwoOrderedHexesIsNotTotal() {
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(grp, Long.class, Arrays.asList(Double.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("there is a cycle between java.lang.Integer and java.lang.String", errs[0]);
		assertEquals("there is no ordering between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.110", acceptanceTests.get(0).getId());
		assertEquals("acceptance.101", acceptanceTests.get(1).getId());
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOrderedHexesIsNotConsistent() {
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(grp, Long.class, Arrays.asList(Integer.class, String.class));
		acc.acceptance(grp, Float.class, Arrays.asList(String.class, Double.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(3, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("there is a cycle between java.lang.Double and java.lang.Integer", errs[0]);
		assertEquals("there is a cycle between java.lang.Double and java.lang.String", errs[1]);
		assertEquals("there is a cycle between java.lang.Integer and java.lang.String", errs[2]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.110", acceptanceTests.get(0).getId());
		assertEquals("acceptance.101", acceptanceTests.get(1).getId());
		assertEquals("acceptance.011", acceptanceTests.get(2).getId());
		assertEquals(3, grp.getClasses().length);
		assertEquals(Float.class.getName(), grp.getClasses()[0]);
		assertEquals(Long.class.getName(), grp.getClasses()[1]);
		assertEquals(String.class.getName(), grp.getClasses()[2]);
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingAfterTwoPasses() {
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Float.class));
		acc.acceptance(grp, String.class, Arrays.asList(Float.class, Integer.class));
		acc.acceptance(grp, String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(4, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.1100", acceptanceTests.get(0).getId());
		assertEquals("acceptance.0110", acceptanceTests.get(1).getId());
		assertEquals("acceptance.0011", acceptanceTests.get(2).getId());
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeConsistentWithOneCoveringAllThree() {
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Integer.class, String.class));
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(grp, String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.111", acceptanceTests.get(0).getId());
		assertEquals("acceptance.110", acceptanceTests.get(1).getId());
		assertEquals("acceptance.011", acceptanceTests.get(2).getId());
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeInonsistentWithOneCoveringAllThreeIfInADifferentOrder() {
		acc.acceptance(grp, String.class, Arrays.asList(Double.class, Integer.class, String.class));
		acc.acceptance(grp, String.class, Arrays.asList(Integer.class, Double.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("ordering between java.lang.Double and java.lang.Integer is inconsistent", errs[0]);
		assertEquals("there is a cycle between java.lang.Double and java.lang.Integer", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.111", acceptanceTests.get(0).getId());
		assertEquals("acceptance.110", acceptanceTests.get(1).getId());
	}
	*/
}
