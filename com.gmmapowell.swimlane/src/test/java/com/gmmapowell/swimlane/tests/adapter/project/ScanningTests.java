package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.project.ProjectScanner;

public class ScanningTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void testThatTheScannerReportsTheRightFiles() throws Exception {
		Accumulator acc = context.mock(Accumulator.class);
		ProjectScanner scanner = new ProjectScanner(acc);
		IJavaProject ijp = context.mock(IJavaProject.class);
		IPath fp = context.mock(IPath.class, "fp");
		IProject ip = context.mock(IProject.class);
		IWorkspace ws = context.mock(IWorkspace.class);
		IWorkspaceRoot wr = context.mock(IWorkspaceRoot.class);
		IFolder ff = context.mock(IFolder.class);
		IPath frp = context.mock(IPath.class, "frp");
		File file = new File("../sample-proj/bin/testclasses");
		IClasspathEntry rce = context.mock(IClasspathEntry.class);
		context.checking(new Expectations() {{
			allowing(ijp).getOutputLocation(); will(returnValue(fp));
			allowing(ijp).getProject(); will(returnValue(ip));
			allowing(ip).getWorkspace(); will(returnValue(ws));
			allowing(ws).getRoot(); will(returnValue(wr));
			allowing(wr).getFolder(fp); will(returnValue(ff));
			allowing(ff).getRawLocation(); will(returnValue(frp));
			allowing(frp).toFile(); will(returnValue(file));
			oneOf(acc).add("com/gmmapowell/swimlane/sample/AcceptanceTest.class");
			allowing(ijp).getRawClasspath(); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(rce).getOutputLocation(); will(returnValue(fp)); // we should have a different path to test better
		}});
		scanner.scan(ijp);
	}
}
