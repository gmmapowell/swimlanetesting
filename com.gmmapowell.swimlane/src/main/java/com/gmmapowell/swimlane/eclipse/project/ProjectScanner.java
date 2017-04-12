package com.gmmapowell.swimlane.eclipse.project;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.gmmapowell.swimlane.eclipse.interfaces.ClassAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectSimplifier;

public class ProjectScanner {
	private final Set<String> scanned = new TreeSet<String>();
	private final ProjectSimplifier resolver;
	private final ClassAnalyzer analyzer;

	public ProjectScanner(ProjectSimplifier resolver, ClassAnalyzer analyzer) {
		this.resolver = resolver;
		this.analyzer = analyzer;
	}

	public void scan(IJavaProject jp) throws JavaModelException {
		scanUnder(jp, jp.getOutputLocation());
		for (IClasspathEntry e : jp.getRawClasspath())
			scanUnder(jp, e.getOutputLocation());
	}

	private void scanUnder(IJavaProject p, IPath op) {
		if (op == null)
			return;
		File root = resolver.resolvePath(op);
		scanAll(root, root);
	}

	private void scanAll(File root, File under) {
		if (under == null)
			return;
		String path = under.getPath();
		if (scanned.contains(path))
			return;
		scanned.add(path);
		if (under.isDirectory()) {
			for (File m : under.listFiles())
				scanAll(root, m);
		} else if (under.isFile() && under.getName().endsWith(".class")) {
			analyzer.consider(className(path.substring(root.getPath().length()+1, path.length()-6)));
		}
	}
	
	private String className(String s) {
		return s.replace('/', '.');
	}
}
