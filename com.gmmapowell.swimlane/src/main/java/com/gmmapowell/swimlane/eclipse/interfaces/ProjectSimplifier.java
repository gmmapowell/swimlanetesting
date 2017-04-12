package com.gmmapowell.swimlane.eclipse.interfaces;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public interface ProjectSimplifier {
	public File resolvePath(IPath path);
}
