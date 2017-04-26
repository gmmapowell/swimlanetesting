package com.gmmapowell.swimlane.eclipse.project;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;

public class BuildListener implements IResourceChangeListener {
	private final List<HexagonModelListener> lsnrs = new ArrayList<>();

	public BuildListener() {
	}
	
	public void addListener(HexagonModelListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		Accumulator model = new HexagonAccumulator();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(SWT.NONE);
		for (IProject p : projects) {
			IJavaProject jp = JavaCore.create(p);
			if (jp != null) {
				try {
					ProjectHelper ph = new ProjectHelper(jp);
					URLClassLoader cl = ph.deduceClasspath();
					ProjectScanner scanner = new ProjectScanner(ph, new HexagonTestAnalyzer(cl, model));
					scanner.scan(jp);
				} catch (JavaModelException e) {
					// TODO: we should capture "problems" with the view
					e.printStackTrace();
				}
			}
		}

		model.setBuildTime(new Date());
		for (HexagonModelListener lsnr : lsnrs)
			lsnr.setModel((HexagonDataModel) model);
	}
}
