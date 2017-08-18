package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo.Type;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;

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
	private final List<PendingNode> pending = new ArrayList<>();
	private final Map<Integer, TestCaseInfo> tests = new HashMap<Integer, TestCaseInfo>();
	private List<String> capture = null;
	private TestCaseInfo cfail = null;
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
			sink.testCount(group, ntests);
//			pending.add(new PendingNode(ntests, new SimpleTree<TestInfo>(new TestCaseInfo(Type.META, group, "", "Top"))));
		} else if (s.startsWith("%TSTTREE")) {
//			if (pending.isEmpty())
//				return;
//				throw new RuntimeException("The orchard dried up - more tests than expected");
//			s = s.substring(8);
//			String[] parts = s.split(",");
//			int tc = Integer.parseInt(parts[0]);
//			boolean isSuite = "true".equals(parts[2]);
//			if (!isSuite) {
//				TestCaseInfo tci = new TestCaseInfo(isSuite?Type.SUITE:Type.TEST, group, extractClassName(parts[1]), extractTestName(parts[1]));
//				tests.put(tc, tci);
//			}
//			Tree<TestInfo> node = new SimpleTree<TestInfo>(tci);
//			pending.get(pending.size()-1).node.add(node);
//			if (isSuite) { // this node is a suite
//				pending.add(new PendingNode(Integer.parseInt(parts[3]), node));
//			} else {
//				Tree<TestInfo> top = pending.get(0).node;
//				for (int i=pending.size()-1;i>=0;i--) {
//					if (--pending.get(i).quant == 0) {
//						if (i != pending.size()-1)
//							throw new RuntimeException("Removed parent node before child");
//						pending.remove(i);
//					}
//				}
//				if (pending.isEmpty())
//					sink.tree(top);
//			}
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
