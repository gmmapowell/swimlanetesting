package com.gmmapowell.swimlane.eclipse.interfaces;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.jdt.core.IJavaProject;

public interface EclipseAbstractor {

	List<IProject> getAllProjects();

	IJavaProject javaProject(IProject p);

	File resolvePath(IJavaProject jp, IPath path);

	List<File> getJunitRunnerClasspathEntries();

	void backgroundWithProgress(IJobFunction job);

	void switchRadio(String toolId, String cmdId, String value);
}
