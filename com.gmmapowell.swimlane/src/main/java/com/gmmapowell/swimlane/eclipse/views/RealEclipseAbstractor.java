package com.gmmapowell.swimlane.eclipse.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;

public class RealEclipseAbstractor implements EclipseAbstractor {

	@Override
	public List<IProject> getAllProjects() {
		return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects(0));
	}

	@Override
	public IJavaProject javaProject(IProject p) {
		return JavaCore.create(p);
	}

	@Override
	public File resolvePath(IJavaProject jp, IPath path) {
		return jp.getProject().getWorkspace().getRoot().getFolder(path).getRawLocation().toFile();
	}

	@Override
	public List<File> getJunitRunnerClasspathEntries() {
		List<File> ret = new ArrayList<File>();
		// JUnitCorePlugin.ID_EXTENSION_POINT_TEST_KINDS
		IExtensionPoint ep = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.jdt.junit.internal_testKinds");
		IExtension[] exts = ep.getExtensions();
		for (int i=0;i<exts.length;i++) {
			IConfigurationElement[] elts = exts[i].getConfigurationElements();
			for (int j=0;j<elts.length;j++) {
				IConfigurationElement[] ces = elts[j].getChildren("runtimeClasspathEntry");
				for (int k=0;k<ces.length;k++) {
					String pluginid = ces[k].getAttribute("pluginId");
					Bundle bundle = Platform.getBundle(pluginid);
					String pathToJar = ces[k].getAttribute("pathToJar");
					if (pathToJar == null)
						pathToJar = "/";
					URL url = bundle.getEntry(pathToJar);
					try {
						File file = new File(FileLocator.toFileURL(url).getFile());
						ret.add(file);
					} catch (IOException ex) {
						// There's not a lot we can do about this
						ex.printStackTrace();
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void backgroundWithProgress(IJobFunction jf) {
		Job job = Job.create("Running Tests ...", jf);
		job.setUser(true);
		job.schedule();
	}
}
