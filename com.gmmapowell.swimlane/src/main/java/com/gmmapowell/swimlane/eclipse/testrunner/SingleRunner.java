package com.gmmapowell.swimlane.eclipse.testrunner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class SingleRunner {
	private final List<String> cmdarray = new ArrayList<String>();
	private TestResultAnalyzer analyzer;

	public SingleRunner(RemoteJUnitTestRunner main, TestResultReporter sink, String classpath, String[] classesUnderTest) throws IOException {
		File file = File.createTempFile("textests", "txt");
		PrintWriter pw = new PrintWriter(file);
		for (String s : classesUnderTest)
			pw.println(s);
		pw.close();
		analyzer = new TestResultAnalyzer(sink);
		cmdarray.add("java");
		cmdarray.add("-XstartOnFirstThread"); // only for Cocoa SWT, but does it really hurt?
		cmdarray.add("-classpath");
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
	}
	
	public void exec() throws Exception {
		TestResultReader trr = new TestResultReader(analyzer);
		Thread thr = new Thread(trr);
		thr.start();

		cmdarray.add(Integer.toString(trr.getPort()));
		ProcessBuilder builder = new ProcessBuilder(cmdarray);
		builder.inheritIO();
		Process proc = builder.start();
		proc.waitFor();

		trr.done();
		thr.interrupt();
		thr.join();
	}


}
