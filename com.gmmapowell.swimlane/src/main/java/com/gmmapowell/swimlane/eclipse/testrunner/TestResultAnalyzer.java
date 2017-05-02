package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
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
	private final TestResultReporter sink;
	private final List<PendingNode> pending = new ArrayList<>();

	public TestResultAnalyzer(TestResultReporter sink) {
		this.sink = sink;
	}

	public void push(String s) {
		System.out.println("Tester sent: " + s);
		if (s.startsWith("%TESTC")) {
			s = s.substring(8);
			String[] codes = s.split(" ");
			if (!"v2".equals(codes[1])) {
				sink.testError("Cannot handle protocol " + codes[1]);
				return;
			}
			pending.add(new PendingNode(Integer.parseInt(codes[0]), new SimpleTree<TestInfo>(new TestCaseInfo("Top"))));
		} else if (s.startsWith("%TSTTREE")) {
			if (pending.isEmpty())
				throw new RuntimeException("The orchard dried up - more tests than expected");
			s = s.substring(8);
			String[] parts = s.split(",");
			Tree<TestInfo> node = new SimpleTree<TestInfo>(new TestCaseInfo(parts[1]));
			pending.get(pending.size()-1).node.add(node);
			if ("true".equals(parts[2])) { // this node is a suite
				pending.add(new PendingNode(Integer.parseInt(parts[3]), node));
			} else {
				Tree<TestInfo> top = pending.get(0).node;
				for (int i=pending.size()-1;i>=0;i--) {
					if (--pending.get(i).quant == 0) {
						if (i != pending.size()-1)
							throw new RuntimeException("Removed parent node before child");
						pending.remove(i);
					}
				}
				if (pending.isEmpty())
					sink.tree(top);
			}
		}
		if (s.startsWith("%RUNTIME"))
			sink.testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
	}

}
