package com.gmmapowell.swimlane.eclipse.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectAnalyzer;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class BuildListener implements IResourceChangeListener {
	private final EclipseAbstractor eclipse;
	private final ProjectAnalyzer analyzer;
	private final ProjectScanner scanner;

	public BuildListener(EclipseAbstractor eclipse, ProjectAnalyzer analyzer) {
		this.eclipse = eclipse;
		this.analyzer = analyzer;
		scanner = new ProjectScanner(analyzer);
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent rce) {
		List<IProject> projects = new ArrayList<>();
		analyzeProjects(projects);
	}

	public void analyzeProjects(List<IProject> projects) {
		analyzer.startAnalysis(eclipse.currentDate());
		for (IProject p : projects) {
			IJavaProject jp = eclipse.javaProject(p);
			if (jp != null) {
				try {
					ProjectHelper ph = new ProjectHelper(eclipse, jp);
					TestGroup grp = new TestGroup(p.getName(), ph.retrieveClasspath());
					scanner.scan(ph, grp, jp);
				} catch (JavaModelException e) {
					// TODO: we should capture "problems" with the view
					e.printStackTrace();
				}
			}
		}

		analyzer.analysisComplete(eclipse.currentDate());
	}
}
