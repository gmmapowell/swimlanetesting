package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.fail;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;

public class AccumulationOfTestResultsTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator eh = context.mock(ErrorAccumulator.class);
	TestResultReporter acc = new SwimlaneModel(eh);
	Solution solution = context.mock(Solution.class);
	ErrorMessageListener errors = context.mock(ErrorMessageListener.class);

//	ModelDispatcher md = new SolidModelDispatcher(null, null);
//	HexagonAccumulator hex = new HexagonAccumulator();
//	TestResultReporter trr = hex; 
//	HexagonDataModel hdm = hex; 
//	TestGroup grp = new TestGroup("Project", null);
//	Class<?> h1 = String.class;
//	List<Class<?>> hexes = new ArrayList<>();
//	Class<?> c1 = Long.class;
//	Class<?> c2 = Integer.class;
//	Class<?> c3 = Float.class;
//	TestInfo test1;
//	TestInfo test2;
//	TestInfo test3;

	@Before
	public void setup() {
//		test1 = context.mock(TestInfo.class, "test1");
//		test2 = context.mock(TestInfo.class, "test2");
//		test3 = context.mock(TestInfo.class, "test3");
//		context.checking(new Expectations() {{
//			allowing(test1).classUnderTest(); will(returnValue(c1.getName()));
//			allowing(test1).groupName(); will(returnValue("Project"));
//			allowing(test2).classUnderTest(); will(returnValue(c2.getName()));
//			allowing(test2).groupName(); will(returnValue("Project"));
//			allowing(test3).classUnderTest(); will(returnValue(c3.getName()));
//			allowing(test3).groupName(); will(returnValue("Project"));
//		}});
//		
//		hex.utility(grp, c1);
//		hex.acceptance(grp, c2, hexes);
//		hex.acceptance(grp, c3, hexes);
//		hex.analysisComplete();
	}

	@Test
	public void testThatABarThatHasNotRunAnyTestsYetGetsAnEmptyCollection() {
		fail("revisit all these");
		/*
		trr.testSuccess(test1);
		assertEquals(0, hdm.getErrors().size());
		Collection<TestResultGroup> acc = hdm.getTestResultsFor("acceptance.1");
		assertNotNull(acc);
		assertEquals(0, acc.size());
		*/
	}

	/*
	@Test
	public void testWeCanRecoverATestGroupGivenTheBarId() {
		trr.testSuccess(test1);
		assertEquals(0, hdm.getErrors().size());
		Collection<TestResultGroup> ute = hdm.getTestResultsFor("utility");
		assertEquals(1, ute.size());
		Set<TestResultClass> tcs = ute.iterator().next().testClasses();
		assertEquals(1, tcs.size());
		TestResultClass trc = tcs.iterator().next();
		assertEquals(Long.class.getName(), trc.className());
		assertEquals(1, trc.tests().size());
		assertEquals(test1, trc.tests().iterator().next());
	}

	@Test
	public void testWeCorrectlyRecoverDistinctTestGroupsGivenTheBarIds() {
		trr.testSuccess(test1);
		trr.testSuccess(test2);
		trr.testSuccess(test3);
		assertEquals(0, hdm.getErrors().size());
		assertEquals(1, hdm.getTestResultsFor("utility").size());
		assertEquals(1, hdm.getTestResultsFor("acceptance.1").size());
	}

	@Test
	public void testThatTests2And3AreInTheSameProject() {
		trr.testSuccess(test1);
		trr.testSuccess(test2);
		trr.testSuccess(test3);
		TestResultGroup trg = hdm.getTestResultsFor("acceptance.1").iterator().next();
		assertEquals("Project", trg.name());
		assertEquals(2, trg.testClasses().size());
	}

	@Test
	public void testThatTests2And3AreInDifferentClasses() {
		trr.testSuccess(test1);
		trr.testSuccess(test2);
		trr.testSuccess(test3);
		TestResultGroup trg = hdm.getTestResultsFor("acceptance.1").iterator().next();
		assertEquals("Project", trg.name());
		Set<TestResultClass> tcs = trg.testClasses();
		Iterator<TestResultClass> tci = tcs.iterator();
		TestResultClass tc1 = tci.next();
		TestResultClass tc2 = tci.next();
		if (tc1.className().equals(Integer.class.getName())) {
			TestResultClass tmp = tc1;
			tc1 = tc2;
			tc2 = tmp;
		}
		assertEquals(Float.class.getName(), tc1.className());
		assertEquals(1, tc1.tests().size());
		assertEquals(test3, tc1.tests().iterator().next());
		assertEquals(Integer.class.getName(), tc2.className());
		assertEquals(1, tc2.tests().size());
		assertEquals(test2, tc2.tests().iterator().next());
	}
	*/
}
