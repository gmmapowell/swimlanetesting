package com.gmmapowell.swimlane.eclipse.views;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;

public class RealEclipseAbstractor implements EclipseAbstractor {

	@Override
	public List<IProject> getAllProjects() {
		return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects(0));
	}

	@Override
	public IJavaProject javaProject(IProject p) {
		return JavaCore.create(p);
	}

	@Override
	public File resolvePath(IJavaProject jp, IPath path) {
		return jp.getProject().getWorkspace().getRoot().getFolder(path).getRawLocation().toFile();
	}
}
