package com.gmmapowell.swimlane.eclipse.project;

import java.net.URLClassLoader;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;

import com.gmmapowell.swimlane.eclipse.analyzer.HexagonTestAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;

public class BuildListener implements IResourceChangeListener {
	private final ModelDispatcher lsnrs;

	public BuildListener(ModelDispatcher lsnrs) {
		this.lsnrs = lsnrs;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		Accumulator acc = new HexagonAccumulator(lsnrs);
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(SWT.NONE);
		for (IProject p : projects) {
			IJavaProject jp = JavaCore.create(p);
			if (jp != null) {
				try {
					ProjectHelper ph = new ProjectHelper(jp);
					URLClassLoader cl = ph.deduceClasspath();
					ProjectScanner scanner = new ProjectScanner(ph, new HexagonTestAnalyzer(cl, acc));
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
