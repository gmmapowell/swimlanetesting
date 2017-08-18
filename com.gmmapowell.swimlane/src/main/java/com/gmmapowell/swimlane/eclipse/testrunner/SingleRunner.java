package com.gmmapowell.swimlane.eclipse.testrunner;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class SingleRunner {
	public static void exec(IProgressMonitor monitor, ErrorAccumulator eh, TestResultReporter sink, GroupOfTests group, String classpath, String... classesUnderTest) throws Exception {

		File file = File.createTempFile("textests", "txt");
		PrintWriter pw = new PrintWriter(file);
		for (String s : classesUnderTest)
			pw.println(s);
		pw.close();

		TestResultAnalyzer analyzer = new TestResultAnalyzer(monitor, eh, sink, group);

		List<String> cmdarray = new ArrayList<String>();
		cmdarray.add("java");
		cmdarray.add("-XstartOnFirstThread"); // only for Cocoa SWT, but does it really hurt?
		cmdarray.add("-classpath");
		System.out.println("classpath = " + classpath);
		cmdarray.add(classpath);
		cmdarray.add("org.eclipse.jdt.internal.junit.runner.RemoteTestRunner");
		cmdarray.add("-version");
		cmdarray.add("3");
		cmdarray.add("-testLoaderClass");
		cmdarray.add("org.eclipse.jdt.internal.junit4.runner.JUnit4TestLoader");
		cmdarray.add("-loaderpluginname");
		cmdarray.add("org.eclipse.jdt.junit4.runtime");
		cmdarray.add("-testNameFile");
		cmdarray.add(file.getPath());
		cmdarray.add("-port");

		System.out.println(new Date() + " starting to run tests");
		TestResultReader trr = new TestResultReader(analyzer);
		trr.start();

		cmdarray.add(Integer.toString(trr.getPort()));
		ProcessBuilder builder = new ProcessBuilder(cmdarray);
		builder.inheritIO();
		Process proc = builder.start();
		proc.waitFor();

		trr.done();
		trr.interrupt();
		trr.join();
		System.out.println(new Date() + " finished running tests");
	}
}
