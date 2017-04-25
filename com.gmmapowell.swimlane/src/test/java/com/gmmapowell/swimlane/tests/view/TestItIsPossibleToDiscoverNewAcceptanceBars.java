package com.gmmapowell.swimlane.tests.view;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.BarData;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel.Status;

public class TestItIsPossibleToDiscoverNewAcceptanceBars extends AViewTest {
	
	@Test
	public void testWeCanAddASecond_Later_Bar() throws Exception {
		BarData a = a();
		pushModel(modelWith("initial", a));
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.acceptance.1");
		pushModel(modelWith("update", a,b()));
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.acceptance.1", "hexagons.acceptance.2");
	}
	
	@Test
	public void testIfWeAddTheLaterBarFirstTheEarlierBarMovesUp() throws Exception {
		BarData b = b();
		pushModel(modelWith("initial", b));
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.acceptance.2");
		pushModel(modelWith("update", b,a()));
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.acceptance.1", "hexagons.acceptance.2");
	}
	
	protected HexagonDataModel modelWith(String s, BarData... bars) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, s);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		for (BarData b : bars)
			accList.add(b);
		context.checking(new Expectations() {{
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
		}});
		return testModel;
	}
	
	protected BarData a() {
		BarData a = context.mock(BarData.class, "ia");
		context.checking(new Expectations() {{
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getStatus(); will(returnValue(Status.OK));
		}});
		return a;
	}
	
	protected BarData b() {
		BarData b = context.mock(BarData.class, "ub");
		context.checking(new Expectations() {{
			allowing(b).getId(); will(returnValue("acceptance.2"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(4));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
		}});
		return b;
	}
}
