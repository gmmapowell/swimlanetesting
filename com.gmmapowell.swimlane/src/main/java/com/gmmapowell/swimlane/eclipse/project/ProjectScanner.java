package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;

public class ProjectScanner {
	private final Set<String> scanned = new TreeSet<String>();
	private final ProjectAnalyzer analyzer;

	public ProjectScanner(ProjectAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void scan(ProjectSimplifier resolver, GroupOfTests grp, IJavaProject jp) throws JavaModelException {
		URLClassLoader cl = resolver.deduceClasspath();
		scanUnder(resolver, cl, grp, jp, jp.getOutputLocation());
		for (IClasspathEntry e : jp.getRawClasspath())
			scanUnder(resolver, cl, grp, jp, e.getOutputLocation());
	}

	private void scanUnder(ProjectSimplifier resolver, URLClassLoader cl, GroupOfTests grp, IJavaProject p, IPath op) {
		if (op == null)
			return;
		File root = resolver.resolvePath(op);
		scanAll(grp, cl, root, root);
	}

	private void scanAll(GroupOfTests grp, URLClassLoader cl, File root, File under) {
		int prefixLen = root.getPath().length()+1;
		if (under == null)
			return;
		String path = under.getPath();
		if (scanned.contains(path))
			return;
		scanned.add(path);
		if (under.isDirectory()) {
			for (File m : under.listFiles())
				scanAll(grp, cl, root, m);
		} else if (under.isFile() && under.getName().endsWith(".class")) {
			analyzer.consider(grp, cl, className(path.substring(prefixLen, path.length()-6)));
		}
	}
	
	private String className(String s) {
		return s.replace('/', '.');
	}
}
