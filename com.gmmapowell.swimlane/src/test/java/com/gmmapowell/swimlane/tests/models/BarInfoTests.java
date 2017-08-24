package com.gmmapowell.swimlane.tests.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.models.BarInfo;

public class BarInfoTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	GroupOfTests grp1 = context.mock(GroupOfTests.class);
	List<String> tests1 = new ArrayList<>();

	@Before
	public void setup() {
		tests1.add("case1");
		tests1.add("case2");
	}


	@Test
	public void anEmptyAcceptanceBarWouldHaveTheRightTooltip() {
		BarInfo bi = new BarInfo();
		assertEquals("Acceptance", bi.getTooltip("acceptance.111"));
	}

	@Test
	public void anAcceptanceBarWith1GroupHasTheRightTooltip() {
		BarInfo bi = new BarInfo();
		bi.testClass(grp1, "TestClass1", tests1);
		assertEquals("Acceptance - 1 group", bi.getTooltip("acceptance.111"));
	}

}
