package com.gmmapowell.swimlane.eclipse.models;

import java.util.ArrayList;
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
			Map<String, Order> t1 = ordering.get(ni);
			for (int j=0;j<names.size();j++) {
				String nj = names.get(j);
				Map<String, Order> t2 = ordering.get(nj);
				Order val = Order.SAME;
				Order t1j = t1.get(nj);
				Order t2i = t2.get(ni);
				if (j < i)
					val = assertBefore(t2i, t1j);
				else if (j > i)
					val = assertBefore(t1j, t2i);
				System.out.println(i + " " + ni + " " + j + " " + nj + " " + t1j + " " + t2i + " " + val);
				t1.put(nj, val);
				t2.put(ni, val.invert());
				dump();
			}
		}
	}

	private void dump() {
		for (Entry<String, Map<String, Order>> s : ordering.entrySet()) {
			System.out.print(s.getKey() + ": ");
			for (Entry<String, Order> c : s.getValue().entrySet())
				System.out.print("("+c+") ");
			System.out.println();
		}
	}

	private Order assertBefore(Order shouldBeBefore, Order shouldBeAfter) {
		System.out.println("Asserting relation of " + shouldBeBefore + " and " + shouldBeAfter);
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
				System.out.println(r.getKey() + " " + c.getKey() + " " + c.getValue());
				if (c.getValue() == Order.NONE) {
					String first = r.getKey();
					String second = c.getKey();
					if (second.compareTo(first) < 0) {
						String tmp = first;
						first = second;
						second = tmp;
					}
					ret.add("There is no ordering between " + first + " and " + second);
				}
			}
		}
		return ret;
	}
}
