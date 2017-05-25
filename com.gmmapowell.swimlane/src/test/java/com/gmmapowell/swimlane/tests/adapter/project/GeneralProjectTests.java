package com.gmmapowell.swimlane.tests.adapter.project;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;
import com.gmmapowell.swimlane.eclipse.project.ProjectHelper;
import com.gmmapowell.swimlane.eclipse.views.RealEclipseAbstractor;

public class GeneralProjectTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void testThatWeCanConstructTheClassPathCorrectly() throws JavaModelException {
		EclipseAbstractor eclipse = context.mock(EclipseAbstractor.class);
		IJavaProject ijp = context.mock(IJavaProject.class);
		IClasspathEntry ic1 = context.mock(IClasspathEntry.class, "ic1");
		IPath odir = context.mock(IPath.class, "odir");
		File file = new File("../sample-proj/bin/testclasses");
		IClasspathEntry ic2 = context.mock(IClasspathEntry.class, "ic2");
		IPath lp1 = context.mock(IPath.class, "lp1"); 
		File lfile = new File("/Library/JRE");
		IClasspathEntry ic3 = context.mock(IClasspathEntry.class, "ic3");
		IPath lp2 = context.mock(IPath.class, "lp2"); 
		File jfile = new File("../sample-proj/lib/foo.jar");

		ProjectSimplifier resolver = context.mock(ProjectSimplifier.class);
		ProjectHelper ph = new ProjectHelper(eclipse, ijp, resolver);
		context.checking(new Expectations() {{
			allowing(ijp).getResolvedClasspath(true); will(returnValue(new IClasspathEntry[] { ic1, ic2, ic3 }));
			allowing(ic1).getEntryKind(); will(returnValue(IClasspathEntry.CPE_SOURCE));
			allowing(ic1).getOutputLocation(); will(returnValue(odir));
			allowing(resolver).resolvePath(odir); will(returnValue(file));
			allowing(ic2).getEntryKind(); will(returnValue(IClasspathEntry.CPE_LIBRARY));
			allowing(ic2).getPath(); will(returnValue(lp1));
			allowing(lp1).toFile(); will(returnValue(lfile));
			allowing(ic3).getEntryKind(); will(returnValue(IClasspathEntry.CPE_LIBRARY));
			allowing(ic3).getPath(); will(returnValue(lp2));
			allowing(lp2).toFile(); will(returnValue(jfile));
			allowing(eclipse).getJunitRunnerClasspathEntries();
		}});
		URLClassLoader cl = ph.deduceClasspath();
		URL[] urls = cl.getURLs();
		assertEquals(3, urls.length);
		assertEquals("file:/Users/gareth/Ziniki/Over/SwimLaneTesting/com.gmmapowell.swimlane/../sample-proj/bin/testclasses/", urls[0].toString());
		assertEquals("file:/Library/JRE", urls[1].toString());
		assertEquals("file:/Users/gareth/Ziniki/Over/SwimLaneTesting/com.gmmapowell.swimlane/../sample-proj/lib/foo.jar", urls[2].toString());
	}

	@Test
	public void testThatPathResolutionWorks() throws Exception {
		EclipseAbstractor eclipse = new RealEclipseAbstractor();
		IJavaProject ijp = context.mock(IJavaProject.class);
		ProjectHelper ph = new ProjectHelper(eclipse, ijp);
		IProject ip = context.mock(IProject.class);
		IWorkspace ws = context.mock(IWorkspace.class);
		IWorkspaceRoot wr = context.mock(IWorkspaceRoot.class);
		IFolder ff = context.mock(IFolder.class);
		IPath frp = context.mock(IPath.class, "frp");
		IPath fp = context.mock(IPath.class, "fp");
		File file = new File("../sample-proj/bin/testclasses");

		context.checking(new Expectations() {{
			oneOf(ijp).getProject(); will(returnValue(ip));
			oneOf(ip).getWorkspace(); will(returnValue(ws));
			oneOf(ws).getRoot(); will(returnValue(wr));
			oneOf(wr).getFolder(fp); will(returnValue(ff));
			oneOf(ff).getRawLocation(); will(returnValue(frp));
			oneOf(frp).toFile(); will(returnValue(file));
		}});

		assertEquals(file, ph.resolvePath(fp));
	}
}
