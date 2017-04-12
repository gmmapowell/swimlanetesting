package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;

public class ProjectScanner {
	private final Set<String> scanned = new TreeSet<String>();
	private final Accumulator accumulator;

	public ProjectScanner(Accumulator accumulator) {
		this.accumulator = accumulator;
	}

	public void scan(IJavaProject jp) {
		try {
			scanUnder(accumulator, jp, jp.getOutputLocation());
			for (IClasspathEntry e : jp.getRawClasspath())
				scanUnder(accumulator, jp, e.getOutputLocation());
		} catch (CoreException e) {
			// TODO: we should capture "problems" with the view
			e.printStackTrace();
		}
	}

	private void scanUnder(Accumulator accumulator, IJavaProject p, IPath op) throws CoreException {
		if (op == null)
			return;
		File root = p.getProject().getWorkspace().getRoot().getFolder(op).getRawLocation().toFile();
		scanAll(accumulator, root, root);
	}

	private void scanAll(Accumulator accumulator, File root, File under) throws CoreException {
		if (under == null)
			return;
		String path = under.getPath();
		if (scanned.contains(path))
			return;
		scanned.add(path);
		System.out.println("Scanning " + under);
		if (under.isDirectory()) {
			for (File m : under.listFiles())
				scanAll(accumulator, root, m);
		} else if (under.isFile() && under.getName().endsWith(".class")) {
			accumulator.add(path.substring(root.getPath().length()+1));
		} else
			System.out.println("Cannot handle " + under.getClass());
	}
}
