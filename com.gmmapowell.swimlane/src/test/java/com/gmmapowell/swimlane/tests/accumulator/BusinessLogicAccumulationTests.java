package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class BusinessLogicAccumulationTests {
	ModelDispatcher md = new SolidModelDispatcher(null, null);
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestGroup grp = new TestGroup("Project", null);
	Class<?> testCase1 = String.class;
	Class<?> testCase2 = Character.class;
	Class<?> testCase3 = Double.class;
	Class<?> accCase = Float.class;
	Class<?> hexClass1 = Integer.class;
	Class<?> hexClass2 = Comparable.class;

	@Test
	public void testThatIfWeAccumulateOneLogicTestTheModelMustHaveTheHexagonForIt() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testWeCanAssociateTestsWithTheDefaultHexagon() {
		acc.logic(grp, testCase1, null);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals("-default-", hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testWeCanAssociateTestsWithTheDefaultHexagonAndNameIt() {
		acc.logic(grp, testCase1, null);
		acc.logic(grp, testCase2, hexClass2);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass2.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testWeCannotAssociateTestsWithTheDefaultHexagonAndAlsoHaveASecondHexagon() {
		acc.acceptance(grp, accCase, Arrays.asList(hexClass1, hexClass2));
		acc.logic(grp, testCase1, null);
		acc.analysisComplete();
		assertEquals(1, hdm.getErrors().size());
		assertTrue(hdm.getErrors().contains("cannot use @BusinessLogic with default hexagon in java.lang.String since there are multiple hexagons"));
		assertEquals(2, hdm.getHexCount());
	}

	@Test
	public void testThatIfWeDontAccumulateAnyLogicTestsThereIsNoBar() {
		acc.adapter(grp, testCase1, hexClass1, Long.class, Exception.class);
		acc.analysisComplete();
		assertNull(hdm.getHexagons().get(0).getBar());
	}

	@Test
	public void testThatIfWeAccumulateAnyLogicTestsThereWillBeABar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertNotNull(hdm.getHexagons().get(0).getBar());
	}

	@Test
	public void testTheBarHasTheRightName() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getBar().getName());
	}

	@Test
	public void testThatOneTestMakesOneEntryInTheBar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
	}

	@Test
	public void testThatTwoTestsMakeTwoEntriesInTheBar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.logic(grp, testCase2, hexClass1);
		acc.analysisComplete();
		assertEquals(2, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
	}

	@Test
	public void testThatTwoTestsInSeparateHexesAreKeptDistinct() {
		acc.logic(grp, testCase1, hexClass1);
		acc.logic(grp, testCase2, hexClass2);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
		assertEquals(1, hdm.getHexagons().get(1).getBar().classesUnderTest().size());
	}
}
