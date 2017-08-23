package com.gmmapowell.swimlane.eclipse.testrunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;

public class TestResultAnalyzer {
	class PendingNode {
		int quant;
		Tree<TestInfo> node;

		public PendingNode(int quant, Tree<TestInfo> node) {
			this.quant = quant;
			this.node = node;
		}
	}
	private final SubMonitor monitor;
	private final TestResultReporter sink;
	private final GroupOfTests group;
	private final ErrorAccumulator eh;
	private TestDelegate delg;

	public TestResultAnalyzer(IProgressMonitor monitor, ErrorAccumulator eh, TestResultReporter sink, GroupOfTests group) {
		this.eh = eh;
		this.group = group;
		this.monitor = SubMonitor.convert(monitor);
		this.sink = sink;
	}

	public void push(String s) {
		System.out.println("tra - " + s);
		if (s.startsWith("%TESTC")) {
			s = s.substring(8);
			String[] codes = s.split(" ");
			if (!"v2".equals(codes[1])) {
				eh.error("Cannot handle protocol " + codes[1]);
				return;
			}
			int ntests = Integer.parseInt(codes[0]);
			monitor.setWorkRemaining(ntests);
		} else if (s.startsWith("%TSTTREE")) {
		} else if (s.startsWith("%TESTS")) {
			monitor.newChild(1);
			if (monitor.isCanceled())
				throw new OperationCanceledException();
			delg = new TestDelegate(group, eh, sink);
		} else if (s.startsWith("%RUNTIME")) {
			// we don't pass this on
			monitor.setWorkRemaining(0);
		} else if (delg == null) {
			eh.error("Encountered unexpected msg from test runner: " + s);
		} else
			delg.push(s);
	}

}
