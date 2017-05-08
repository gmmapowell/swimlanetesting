package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;

public class ProjectHelper implements ProjectSimplifier {
	private final EclipseAbstractor eclipse;
	private final IJavaProject jp;
	private final ProjectSimplifier ph;

	public ProjectHelper(EclipseAbstractor eclipse, IJavaProject jp) {
		this.eclipse = eclipse;
		this.jp = jp;
		this.ph = this;
	}

	public ProjectHelper(EclipseAbstractor eclipse, IJavaProject jp, ProjectSimplifier resolver) {
		this.eclipse = eclipse;
		this.jp = jp;
		this.ph = resolver;
	}

	public URLClassLoader deduceClasspath() throws JavaModelException {
		URL[] urls = urlsFrom(retrieveClasspath());
		return new URLClassLoader(urls);
	}

	protected List<File> retrieveClasspath() throws JavaModelException {
		List<File> classpath = new ArrayList<File>();
		IClasspathEntry[] resolvedClasspath = jp.getResolvedClasspath(true);
		for (IClasspathEntry e : resolvedClasspath) {
			File tmp = null;
			if (e.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				if (e.getOutputLocation() != null)
					tmp = ph.resolvePath(e.getOutputLocation());
				else
					tmp = ph.resolvePath(jp.getOutputLocation());
			} else if (e.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				tmp = e.getPath().toFile();
			}
			if (tmp != null && !classpath.contains(tmp))
				classpath.add(tmp);
		}
		return classpath;
	}
    
	public URL[] urlsFrom(List<File> classpath) {
		URL[] ret = new URL[classpath.size()];
		for (int i = 0; i < classpath.size(); i++) {
			try {
				ret[i] = classpath.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO: should capture these errors
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	@Override
	public File resolvePath(IPath path) {
		return eclipse.resolvePath(jp, path);
	}
}
