package com.gmmapowell.swimlane.eclipse.testrunner;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class SingleRunner {
	private static final Logger logger = LoggerFactory.getLogger("JUnitRunner");

	public static void exec(IProgressMonitor monitor, ErrorAccumulator eh, TestResultReporter sink, GroupOfTests group, String classpath, String... classesUnderTest) throws Exception {
		if (classesUnderTest.length == 0) {
			logger.info("There are no tests to run");
			return;
		}
			
		File file = File.createTempFile("textests", ".txt");
		file.deleteOnExit();
		try {
			PrintWriter pw = new PrintWriter(file);
			for (String s : classesUnderTest)
				pw.println(s);
			pw.close();
	
			TestResultAnalyzer analyzer = new TestResultAnalyzer(monitor, eh, sink, group);
			TestResultReader trr = new TestResultReader(analyzer);
			trr.start();
	
			List<String> cmdarray = new ArrayList<String>();
			cmdarray.add("java");
//			cmdarray.add("-verbose:class");
			cmdarray.add("-XstartOnFirstThread"); // only for Cocoa SWT, but does it really hurt?
			cmdarray.add("-classpath");
//			logger.info("classpath = " + classpath);
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
			cmdarray.add(Integer.toString(trr.getPort()));
	
			logger.info("starting to run tests");
			for (String s : cmdarray)
				logger.info("  arg: '" + s + "'");
	
			ProcessBuilder builder = new ProcessBuilder(cmdarray);
			builder.inheritIO();
			Process proc = builder.start();
			proc.waitFor();
	
			trr.done();
			trr.interrupt();
			trr.join();
			System.out.println(new Date() + " finished running tests");
		} finally {
			file.delete();
		}
	}
}
