package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;
import com.gmmapowell.swimlane.eclipse.project.ProjectScanner;

public class ScanningTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void testThatTheScannerReportsTheRightFiles() throws Exception {
		GroupOfTests grp = context.mock(GroupOfTests.class);
		ProjectSimplifier resolver = context.mock(ProjectSimplifier.class);
		ProjectAnalyzer analyzer = context.mock(ProjectAnalyzer.class);
		ProjectScanner scanner = new ProjectScanner(analyzer);
		IJavaProject ijp = context.mock(IJavaProject.class);
		URLClassLoader cp = new URLClassLoader(new URL[0]);  // should we mock this?  is it worth it?
		IPath fp = context.mock(IPath.class, "fp");
		File root = new File("../sample-proj/bin/testclasses");
		IClasspathEntry rce = context.mock(IClasspathEntry.class);
		context.checking(new Expectations() {{
			allowing(resolver).deduceClasspath(); will(returnValue(cp));
			allowing(ijp).getOutputLocation(); will(returnValue(fp));
			allowing(resolver).resolvePath(fp); will(returnValue(root));
			allowing(ijp).getRawClasspath(); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(rce).getOutputLocation(); will(returnValue(fp)); // we should have a different path to test better
			oneOf(analyzer).consider(grp, cp, "com.gmmapowell.swimlane.sample.tests.AcceptanceTest");
			oneOf(analyzer).consider(grp, cp, "com.gmmapowell.swimlane.sample.tests.AcceptanceTestWithThreeHexagons");
			oneOf(analyzer).consider(grp, cp, "com.gmmapowell.swimlane.sample.tests.AdapterTest");
			oneOf(analyzer).consider(grp, cp, "com.gmmapowell.swimlane.sample.tests.UtilityTest");
		}});
		scanner.scan(resolver, grp, ijp);
	}

}
