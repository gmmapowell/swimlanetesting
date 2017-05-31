package com.gmmapowell.swimlane.tests.view.hex;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;

public class DiscoveringNewAcceptanceBars extends BaseViewTest {
	
	@Test
	public void testWeCanAddASecond_Later_Bar() throws Exception {
		BarData a = a();
		pushModel(modelWith("initial", a));
		assertControlsInOrder(shell, "hexagons.acceptance.11");
		pushModel(modelWith("update", a,b()));
		assertControlsInOrder(shell, "hexagons.acceptance.11", "hexagons.acceptance.10");
	}
	
	@Test
	public void testIfWeAddTheLaterBarFirstTheEarlierBarMovesUp() throws Exception {
		BarData b = b();
		pushModel(modelWith("initial", b));
		assertControlsInOrder(shell, "hexagons.acceptance.10");
		pushModel(modelWith("update", b,a()));
		assertControlsInOrder(shell, "hexagons.acceptance.11", "hexagons.acceptance.10");
	}
	
	protected HexagonDataModel modelWith(String s, BarData... bars) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, s);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		for (BarData b : bars)
			accList.add(b);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
		}});
		return testModel;
	}
	
	protected BarData a() {
		BarData a = context.mock(BarData.class, "ia");
		context.checking(new Expectations() {{
			allowing(a).getId(); will(returnValue("acceptance.11"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));

			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
		}});
		return a;
	}
	
	protected BarData b() {
		BarData b = context.mock(BarData.class, "ub");
		context.checking(new Expectations() {{
			allowing(b).getId(); will(returnValue("acceptance.10"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(4));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
			allowing(b).getMarks(); will(returnValue(new int[] { 1 }));
			
			oneOf(md).addBarListener(with(b), with(aNonNull(BarDataListener.class)));
		}});
		return b;
	}
}
