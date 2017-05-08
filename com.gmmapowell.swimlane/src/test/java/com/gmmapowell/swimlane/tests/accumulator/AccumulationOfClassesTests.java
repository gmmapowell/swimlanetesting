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
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class AccumulationOfClassesTests extends TestBase {
	ModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	TestGroup grp = new TestGroup(null);

	@Test
	public void ifWeDontPutAnythingInWeGetNothingOut() {
		List<TestGroup> groups = acc.getAllTestClasses();
		assertTrue(groups.isEmpty());
	}

	@Test
	public void ifWePutOneAcceptanceClassInWeGetItOut() {
		acc.acceptance(grp, Integer.class, Arrays.asList());
		List<TestGroup> groups = acc.getAllTestClasses();
		assertEquals(1, groups.size());
		assertEquals(1, groups.get(0).getClasses().length);
		assertEquals("java.lang.Integer", groups.get(0).getClasses()[0]);
	}
}
