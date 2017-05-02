package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.Tree;

public class SimpleTree<T> implements Tree<T> {
	private final T me;
	private final List<Tree<T>> children = new ArrayList<Tree<T>>();

	public SimpleTree(T me) {
		this.me = me;
	}

	@Override
	public void add(Tree<T> child) {
		children.add(child);
	}
	
	@Override
	public T me() {
		return me;
	}
	
	@Override
	public List<Tree<T>> children() {
		return children;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		asString(sb);
		return sb.toString();
	}

	private void asString(StringBuilder sb) {
		sb.append("Tree ");
		sb.append(me);
		sb.append(" [");
		String sep = "";
		for (Tree<T> c : children) {
			((SimpleTree<T>)c).asString(sb);
			sb.append(sep);
			sep = ",";
		}
		sb.append("]");
	}
}
