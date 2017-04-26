package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TotalOrder {
	public enum Order {
		NONE {
			public Order invert() { return NONE; }
		},
		BEFORE {
			public Order invert() { return AFTER; }
		},
		SAME {
			public Order invert() { return SAME; }
		},
		AFTER {
			public Order invert() { return BEFORE; }
		},
		INCONSISTENT {
			public Order invert() { return Order.INCONSISTENT; }
		};

		public abstract Order invert();
	}

	private boolean haveDefault = false;
	// For two states, "A" and "B", this expresses the relationship that they have in *ALL* acceptance statements
	// So if "A" always comes before "B" then ordering[A][B] = Before and ordering[B][A] = After
	private Map<String, Map<String, Order>> ordering = new TreeMap<String, Map<String, Order>>();

	public void haveDefault() {
		haveDefault = true;
	}

	public void addAll(List<Class<?>> hexes) {
		List<String> names = new ArrayList<String>();
		for (Class<?> cls : hexes) {
			String name = cls.getName();
			names.add(name);
			if (!ordering.containsKey(name)) {
				TreeMap<String, Order> map = new TreeMap<>();
				map.put(name, Order.SAME);
				for (String n : ordering.keySet()) {
					map.put(n, Order.NONE);
					ordering.get(n).put(name, Order.NONE);
				}
				ordering.put(name, map);
			}
		}
		for (int i=0;i<names.size();i++) {
			String ni = names.get(i);
			Map<String, Order> ti = ordering.get(ni);
			for (int j=0;j<names.size();j++) {
				String nj = names.get(j);
				Map<String, Order> tj = ordering.get(nj);
				Order val = Order.SAME;
				Order tij = ti.get(nj);
				Order tji = tj.get(ni);
				if (j < i)
					val = assertBefore(tij, tji);
				else if (j > i)
					val = assertBefore(tji, tij).invert();
				ti.put(nj, val);
				tj.put(ni, val.invert());
			}
		}
	}

	private Order assertBefore(Order shouldBeBefore, Order shouldBeAfter) {
		if ((shouldBeBefore == Order.NONE || shouldBeBefore == Order.BEFORE) &&
			(shouldBeAfter == Order.NONE || shouldBeAfter == Order.AFTER))
			return Order.BEFORE;
		return Order.INCONSISTENT;
	}

	public int count() {
		if (!ordering.isEmpty())
			return ordering.size();
		return haveDefault?1:0;
	}

	public Set<? extends String> ensureTotalOrdering() {
		Set<String> ret = new TreeSet<String>();
		for (Entry<String, Map<String, Order>> r : ordering.entrySet()) {
			for (Entry<String, Order> c : r.getValue().entrySet()) {
				String first = r.getKey();
				String second = c.getKey();
				if (second.compareTo(first) < 0) {
					String tmp = first;
					first = second;
					second = tmp;
				}
				if (c.getValue() == Order.NONE) {
					ret.add("There is no ordering between " + first + " and " + second);
				} else if (c.getValue() == Order.INCONSISTENT) {
					ret.add("Ordering between " + first + " and " + second + " is inconsistent");
				}
			}
		}
		return ret;
	}

	public List<String> bestOrdering() {
		List<String> ret = new ArrayList<String>();
		while (ret.size() < ordering.size()) {
			int cnt = -1;
			String best = null;
			for (Entry<String, Map<String, Order>> r : ordering.entrySet()) {
				if (ret.contains(r.getKey())) // don't add things twice
					continue;
				int mc = countBefores(r.getValue().values());
				if (mc > cnt) {
					cnt = mc;
					best = r.getKey();
				}
			}
			if (cnt == -1)
				throw new RuntimeException("That should not be possible");
			ret.add(best);
		}
		if (ret.isEmpty() && haveDefault)
			ret.add(null);
		return ret;
	}

	private int countBefores(Collection<Order> collection) {
		int ret = 0;
		for (Order o : collection)
			if (o == Order.BEFORE)
				ret++;
		return ret;
	}
}