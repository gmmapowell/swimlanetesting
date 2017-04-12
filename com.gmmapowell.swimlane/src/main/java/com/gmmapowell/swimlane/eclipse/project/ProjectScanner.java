package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;

public class ProjectScanner {
	private final Set<String> scanned = new TreeSet<String>();
	private final Accumulator accumulator;
	private final ProjectSimplifier resolver;

	public ProjectScanner(Accumulator accumulator, ProjectSimplifier resolver) {
		this.accumulator = accumulator;
		this.resolver = resolver;
	}

	public void scan(IJavaProject jp) throws JavaModelException {
		scanUnder(accumulator, jp, jp.getOutputLocation());
		for (IClasspathEntry e : jp.getRawClasspath())
			scanUnder(accumulator, jp, e.getOutputLocation());
	}

	private void scanUnder(Accumulator accumulator, IJavaProject p, IPath op) {
		if (op == null)
			return;
		File root = resolver.resolvePath(op);
		scanAll(accumulator, root, root);
	}

	private void scanAll(Accumulator accumulator, File root, File under) {
		if (under == null)
			return;
		String path = under.getPath();
		if (scanned.contains(path))
			return;
		scanned.add(path);
		if (under.isDirectory()) {
			for (File m : under.listFiles())
				scanAll(accumulator, root, m);
		} else if (under.isFile() && under.getName().endsWith(".class")) {
			accumulator.add(path.substring(root.getPath().length()+1));
		}
	}
}
