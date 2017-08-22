package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.jmock.Expectations;
import org.jmock.States;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.views.BarControl;

public class RunningTestsCanUpdateBars extends BaseViewTest {
	@Test
	public void testTheBarCanBeRedrawnProgressivelyMoreGreen() throws Exception {
		shell.setLayout(new GridLayout(1, false));
		BarControl bar = new BarControl(shell, "anybar");
		bar.getCanvas().setLayoutData(new GridData(590, 6));
		bar.getCanvas().setSize(590, 6);
		displayHelper.flushPendingEvents();
//		Canvas check = waitForControl(shell, "swimlane.bar.anybar");
//		System.out.println(check.getSize());
		States progress = context.states("progress");
		progress.become("t0");
		BarData bi = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(bi).isPassing(); will(returnValue(true));
			allowing(bi).getTotal(); will(returnValue(10));
			allowing(bi).getComplete(); will(returnValue(0)); when(progress.is("t0"));
		}});
		bar.barChanged(bi);

		// This is what I would like to check:
//		checkSizeColors(check, 590, 6, new ImageChecker() {
//		@Override
//		public void checkImage(ImageProxy proxy) {
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 105, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
//		}
//	});

		// This is what I have to check:
		assertEquals(590, bar.total());
		assertEquals(0, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());
		
		// moving on
		context.checking(new Expectations() {{
			allowing(bi).getComplete(); will(returnValue(3)); when(progress.is("t1"));
		}});
		progress.become("t1");
		bar.barChanged(bi);

		assertEquals(590, bar.total());
		assertEquals(177, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());

		// moving to the end
		context.checking(new Expectations() {{
			allowing(bi).getComplete(); will(returnValue(10)); when(progress.is("t2"));
		}});
		progress.become("t2");
		bar.barChanged(bi);

		assertEquals(590, bar.total());
		assertEquals(590, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());
	}
	
	@Test
	public void testTheBarCanChangeFromGreenToRed() throws Exception {
		shell.setLayout(new GridLayout(1, false));
		BarControl bar = new BarControl(shell, "anybar");
		bar.getCanvas().setLayoutData(new GridData(590, 6));
		bar.getCanvas().setSize(590, 6);
		displayHelper.flushPendingEvents();
//		Canvas check = waitForControl(shell, "swimlane.bar.anybar");
//		System.out.println(check.getSize());
		States progress = context.states("progress");
		progress.become("green");
		BarData bi = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(bi).isPassing(); will(returnValue(true)); when(progress.is("green"));
			allowing(bi).getTotal(); will(returnValue(10));
			allowing(bi).getComplete(); will(returnValue(0)); when(progress.is("green"));
		}});
		bar.barChanged(bi);

		// This is what I would like to check:
//		checkSizeColors(check, 590, 6, new ImageChecker() {
//		@Override
//		public void checkImage(ImageProxy proxy) {
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 105, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
//		}
//	});

		// This is what I have to check:
		assertEquals(590, bar.total());
		assertEquals(0, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());
		
		// moving on
		context.checking(new Expectations() {{
			allowing(bi).isPassing(); will(returnValue(false)); when(progress.is("red"));
			allowing(bi).getComplete(); will(returnValue(5)); when(progress.is("red"));
		}});
		progress.become("red");
		bar.barChanged(bi);

		assertEquals(590, bar.total());
		assertEquals(295, bar.progress());
		assertEquals(SWT.COLOR_RED, bar.color());
	}
	
	@Test
	public void testTheBarCanCopeWithNewTestsAppearing() throws Exception {
		shell.setLayout(new GridLayout(1, false));
		BarControl bar = new BarControl(shell, "anybar");
		bar.getCanvas().setLayoutData(new GridData(590, 6));
		bar.getCanvas().setSize(590, 6);
		displayHelper.flushPendingEvents();
//		Canvas check = waitForControl(shell, "swimlane.bar.anybar");
//		System.out.println(check.getSize());
		States progress = context.states("progress");
		progress.become("before");
		BarData bi = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(bi).isPassing(); will(returnValue(true));
			allowing(bi).getTotal(); will(returnValue(10)); when(progress.is("before"));
			allowing(bi).getComplete(); will(returnValue(5));
		}});
		bar.barChanged(bi);

		// This is what I would like to check:
//		checkSizeColors(check, 590, 6, new ImageChecker() {
//		@Override
//		public void checkImage(ImageProxy proxy) {
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 105, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
//			proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
//		}
//	});

		// This is what I have to check:
		assertEquals(590, bar.total());
		assertEquals(295, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());
		
		// moving on
		context.checking(new Expectations() {{
			allowing(bi).getTotal(); will(returnValue(12)); when(progress.is("after"));
		}});
		progress.become("after");
		bar.barChanged(bi);

		assertEquals(590, bar.total());
		assertEquals(245, bar.progress());
		assertEquals(SWT.COLOR_GREEN, bar.color());
	}
}
