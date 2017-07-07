package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class BuildListener implements IResourceChangeListener {
	private final ModelDispatcher lsnrs;
	private final EclipseAbstractor eclipse;

	public BuildListener(ModelDispatcher lsnrs, EclipseAbstractor eclipse) {
		this.lsnrs = lsnrs;
		this.eclipse = eclipse;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		Accumulator acc = new HexagonAccumulator(lsnrs);
		List<IProject> projects = eclipse.getAllProjects();
		for (IProject p : projects) {
			IJavaProject jp = eclipse.javaProject(p);
			if (jp != null) {
				try {
					ProjectHelper ph = new ProjectHelper(eclipse, jp);
					List<File> cp = ph.retrieveClasspath();
					URLClassLoader cl = new URLClassLoader(ph.urlsFrom(cp));
					ProjectScanner scanner = new ProjectScanner(ph, new HexagonTestAnalyzer(new TestGroup(p.getName(), cp), cl, acc));
					scanner.scan(jp);
				} catch (JavaModelException e) {
					// TODO: we should capture "problems" with the view
					e.printStackTrace();
				}
			}
		}

		acc.setBuildTime(new Date());
		acc.analysisComplete();
		lsnrs.setModel((HexagonDataModel) acc);
	}
}
