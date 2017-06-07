package com.gmmapowell.swimlane.fakes;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.jdt.core.IJavaProject;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;

public class FakeEclipseAbstractor implements EclipseAbstractor {

	@Override
	public List<IProject> getAllProjects() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public IJavaProject javaProject(IProject p) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public File resolvePath(IJavaProject jp, IPath path) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public List<File> getJunitRunnerClasspathEntries() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void backgroundWithProgress(IJobFunction job) {
		job.run(null);
	}

	@Override
	public void switchRadio(String toolId, String cmdId, String value) {
		throw new RuntimeException("Not implemented");
	}

}
