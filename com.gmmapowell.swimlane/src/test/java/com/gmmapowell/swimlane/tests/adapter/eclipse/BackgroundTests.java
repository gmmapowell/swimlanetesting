package com.gmmapowell.swimlane.tests.adapter.eclipse;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.RealEclipseAbstractor;

public class BackgroundTests {

	@Test
	public void test() {
		RealEclipseAbstractor eclipse = new RealEclipseAbstractor();
		eclipse.backgroundWithProgress(new IJobFunction() {
			@Override
			public IStatus run(IProgressMonitor ipm) {
				SubMonitor monitor = SubMonitor.convert(ipm);
				monitor.setWorkRemaining(4);
				monitor.newChild(2);
				monitor.newChild(2);
				return Status.OK_STATUS;
			}
		});
	}

}
