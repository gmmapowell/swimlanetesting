package com.gmmapowell.swimlane.tests.adapter.project;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ClassAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.project.BuildListener;
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
			oneOf(acc).consider("com.gmmapowell.swimlane.sample.tests.AcceptanceTest");
			oneOf(acc).consider("com.gmmapowell.swimlane.sample.tests.AcceptanceTestWithThreeHexagons");
			allowing(ijp).getRawClasspath(); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(rce).getOutputLocation(); will(returnValue(fp)); // we should have a different path to test better
		}});
		scanner.scan(ijp);
	}

	@Test
	public void testThatTheScannerCanBeInvokedThroughTheBuildListener() throws Exception {
		ModelDispatcher lsnrs = context.mock(ModelDispatcher.class);
		EclipseAbstractor eclipse = context.mock(EclipseAbstractor.class);
		BuildListener bl = new BuildListener(lsnrs, eclipse);
		IProject ip = context.mock(IProject.class);
		IJavaProject ijp = context.mock(IJavaProject.class);
		IPath fp = context.mock(IPath.class, "fp");
		File root = new File("../sample-proj/bin/testclasses");
		IClasspathEntry rce = context.mock(IClasspathEntry.class);
		context.checking(new Expectations() {{
			allowing(eclipse).getAllProjects(); will(returnValue(Arrays.asList(ip)));
			allowing(eclipse).javaProject(ip); will(returnValue(ijp));
			allowing(ijp).getProject(); will(returnValue(ip));
			allowing(rce).getEntryKind(); will(returnValue(IClasspathEntry.CPE_SOURCE));
			allowing(ijp).getResolvedClasspath(true); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(ijp).getOutputLocation(); will(returnValue(fp));
			allowing(eclipse).resolvePath(ijp, fp); will(returnValue(root));
			allowing(ijp).getRawClasspath(); will(returnValue(new IClasspathEntry[] { rce }));
			allowing(rce).getOutputLocation(); will(returnValue(fp)); // we should have a different path to test better
			oneOf(lsnrs).setModel(with(any(HexagonAccumulator.class)));
			allowing(eclipse).getJunitRunnerClasspathEntries();
		}});
		bl.resourceChanged(null);
	}

}
