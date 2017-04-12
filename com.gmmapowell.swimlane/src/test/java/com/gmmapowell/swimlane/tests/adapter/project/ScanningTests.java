package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ClassAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;
import com.gmmapowell.swimlane.eclipse.project.ProjectScanner;

public class ScanningTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void testThatTheScannerReportsTheRightFiles() throws Exception {
		ClassAnalyzer acc = context.mock(ClassAnalyzer.class);
		ProjectSimplifier resolver = context.mock(ProjectSimplifier.class);
		ProjectScanner scanner = new ProjectScanner(resolver, acc);
		IJavaProject ijp = context.mock(IJavaProject.class);
		IPath fp = context.mock(IPath.class, "fp");
		File root = new File("../sample-proj/bin/testclasses");
		IClasspathEntry rce = context.mock(IClasspathEntry.class);
		context.checking(new Expectations() {{
			allowing(ijp).getOutputLocation(); will(returnValue(fp));
			allowing(resolver).resolvePath(fp); will(returnValue(root));
			oneOf(acc).consider("com.gmmapowell.swimlane.sample.AcceptanceTest");
			allowing(ijp).getRawClasspath(); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(rce).getOutputLocation(); will(returnValue(fp)); // we should have a different path to test better
		}});
		scanner.scan(ijp);
	}
}
