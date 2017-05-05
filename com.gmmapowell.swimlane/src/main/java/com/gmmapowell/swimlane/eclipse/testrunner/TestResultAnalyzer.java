package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final TestResultReporter sink;
	private final List<PendingNode> pending = new ArrayList<>();
	private final Map<Integer, TestCaseInfo> tests = new HashMap<Integer, TestCaseInfo>();
	private List<String> capture = null;
	private TestCaseInfo cfail = null;

	public TestResultAnalyzer(TestResultReporter sink) {
		this.sink = sink;
	}

	public void push(String s) {
		if (s.startsWith("%TRACEE")) {
			cfail.stack(capture);
			capture = null;
		} else if (s.startsWith("%EXPECTE")) {
			cfail.expectedValue(String.join("\n", capture));
			capture = null;
		} else if (s.startsWith("%ACTUALE")) {
			cfail.actualValue(String.join("\n", capture));
			capture = null;
		} else if (capture != null) {
			capture.add(s);
		} else if (s.startsWith("%TESTC")) {
			s = s.substring(8);
			String[] codes = s.split(" ");
			if (!"v2".equals(codes[1])) {
				sink.testError("Cannot handle protocol " + codes[1]);
				return;
			}
			pending.add(new PendingNode(Integer.parseInt(codes[0]), new SimpleTree<TestInfo>(new TestCaseInfo(Type.META, "", "Top"))));
		} else if (s.startsWith("%TSTTREE")) {
			if (pending.isEmpty())
				throw new RuntimeException("The orchard dried up - more tests than expected");
			s = s.substring(8);
			String[] parts = s.split(",");
			int tc = Integer.parseInt(parts[0]);
			boolean isSuite = "true".equals(parts[2]);
			TestCaseInfo tci = new TestCaseInfo(isSuite?Type.SUITE:Type.TEST, extractClassName(parts[1]), extractTestName(parts[1]));
			tests.put(tc, tci);
			Tree<TestInfo> node = new SimpleTree<TestInfo>(tci);
			pending.get(pending.size()-1).node.add(node);
			if (isSuite) { // this node is a suite
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
		} else if (s.startsWith("%FAILED")) {
			s = s.substring(8);
			String[] parts = s.split(",");
			int tc = Integer.parseInt(parts[0]);
			cfail = tests.get(tc);
			cfail.failed();
		} else if (s.startsWith("%TRACES") || s.startsWith("%ACTUALS") || s.startsWith("%EXPECTS")) {
			capture = new ArrayList<String>();
		} else if (s.startsWith("%TESTS")) {
			// I don't think we need to do anything unless we want to estimate time-per-test
		} else if (s.startsWith("%TESTE")) {
			s = s.substring(8);
			String[] parts = s.split(",");
			int tc = Integer.parseInt(parts[0]);
			TestInfo ti = tests.get(tc);
			if (ti.hasFailed())
				sink.testFailure(ti);
			else
				sink.testSuccess(ti);
		} else if (s.startsWith("%RUNTIME")) {
			s = s.substring(8);
			sink.testRuntime(Integer.parseInt(s));
		} else
			System.err.println("Encountered unexpected msg from test runner: " + s);
	}

	private String extractTestName(String name) {
		int idx = name.indexOf("(");
		if (idx == -1)
			return name;
		else
			return name.substring(0, idx);
	}

	private String extractClassName(String name) {
		int idx = name.indexOf("(");
		int idx2 = name.indexOf(")");
		if (idx == -1)
			return name;
		else
			return name.substring(idx+1, idx2);
	}

}
