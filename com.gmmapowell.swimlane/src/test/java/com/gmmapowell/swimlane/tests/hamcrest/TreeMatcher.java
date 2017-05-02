package com.gmmapowell.swimlane.tests.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;

public class TreeMatcher extends BaseMatcher<Tree<TestInfo>> {
	private Tree<TestInfo> tree;

	private TreeMatcher(Tree<TestInfo> tree) {
		this.tree = tree;
	}

	@Override
	public boolean matches(Object item) {
		if (!(item instanceof Tree))
			return false;
		@SuppressWarnings("unchecked")
		Tree<TestInfo> actual = (Tree<TestInfo>) item;
		return compareTrees(this.tree, actual);
	}

	protected boolean compareTrees(Tree<TestInfo> expected, Tree<TestInfo> actual) {
		if (!TestInfoMatcher.of(expected.me(), false).matches(actual.me()))
			return false;
		if (expected.children().size() != actual.children().size())
			return false;
		for (int i=0;i<expected.children().size();i++)
			if (!compareTrees(expected.children().get(i), actual.children().get(i)))
				return false;
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(this.tree);
	}

	public static TreeMatcher of(Tree<TestInfo> tree) {
		return new TreeMatcher(tree);
	}

}
