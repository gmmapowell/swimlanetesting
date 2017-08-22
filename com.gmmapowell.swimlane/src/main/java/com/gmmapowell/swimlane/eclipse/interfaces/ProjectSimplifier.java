package com.gmmapowell.swimlane.eclipse.interfaces;

import java.io.File;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaModelException;

public interface ProjectSimplifier {
	public File resolvePath(IPath path);
	public URLClassLoader deduceClasspath() throws JavaModelException;
}
