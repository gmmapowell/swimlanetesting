package com.gmmapowell.swimlane.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.RunningJob;
import com.gmmapowell.swimlane.eclipse.views.SwimlaneViewPart;

public class RealEclipseAbstractor implements EclipseAbstractor {
	@Override
	public Date currentDate() {
		return new Date();
	}

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
	public RunningJob backgroundWithProgressLocked(ISchedulingRule rule, IJobFunction jf) {
		Job job = Job.create("Running Tests ...", jf);
		job.setRule(rule);
		job.setUser(true);
		job.schedule();
		return new BackgroundJob(job);
	}

	@Override
	public void switchRadio(String toolId, String cmdId, String value) {
		try {
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow iww = wb.getActiveWorkbenchWindow();
			SwimlaneViewPart hvp = (SwimlaneViewPart) iww.getActivePage().findView(SwimlaneViewPart.ID);
			IToolBarManager tbm = hvp.getViewSite().getActionBars().getToolBarManager();
			ContributionItem tr = (ContributionItem)tbm.find("com.gmmapowell.swimlane.eclipse.toolbar.TestResults");
			tr.update();
//			WorkbenchWindow ww = (WorkbenchWindow) iww;
			
			ICommandService commandService = (ICommandService) iww.getService(
							ICommandService.class);
			Command command = commandService
					.getCommand("com.gmmapowell.swimlane.eclipse.commands.TestResults");
//			ExecutionEvent ev = new ExecutionEvent(command, new HashMap<>(), null, null);
//			command.getHandler().execute(ev);
//			System.out.println(command.getState(RadioState.STATE_ID).getValue());
			HandlerUtil.updateRadioState(command, "Tests");
//			System.out.println(command.getState(RadioState.STATE_ID).getValue());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
