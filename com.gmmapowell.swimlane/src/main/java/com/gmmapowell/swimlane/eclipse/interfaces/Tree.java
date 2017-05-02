package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface Tree<T> {
	void add(Tree<T> child);
	T me();
	List<Tree<T>> children();
}
