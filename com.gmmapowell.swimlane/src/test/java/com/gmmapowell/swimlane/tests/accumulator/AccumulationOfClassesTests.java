package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class AccumulationOfClassesTests {
	ModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);

	@Test
	public void ifWeDontPutAnythingInWeGetNothingOut() {
		List<TestGroup> groups = acc.getAllTestClasses();
		assertTrue(groups.isEmpty());
	}

	@Test
	public void ifWePutOneAcceptanceClassInWeGetItOut() {
		acc.acceptance(Integer.class, Arrays.asList());
		List<TestGroup> groups = acc.getAllTestClasses();
		assertEquals(1, groups.size());
	}
}
