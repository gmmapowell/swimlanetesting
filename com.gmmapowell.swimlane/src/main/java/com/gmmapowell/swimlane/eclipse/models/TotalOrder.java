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

	public void add(String name) {
		if (ordering.containsKey(name))
			return;
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

	public void addAll(List<String> hexes) {
		for (String cls : hexes) {
			add(cls);
		}
		for (int i=0;i<hexes.size();i++) {
			String ni = hexes.get(i);
			Map<String, Order> ti = ordering.get(ni);
			for (int j=0;j<hexes.size();j++) {
				String nj = hexes.get(j);
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

	public void ensureTotalOrdering(Set<String> ret) {
		Set<String> none = new TreeSet<String>();

		// Perform a transitive closure while there are no errors
		while (ret.isEmpty()) {
			int nchanged = 0;
			none.clear();
			for (Entry<String, Map<String, Order>> r : ordering.entrySet()) {
				for (Entry<String, Order> c : r.getValue().entrySet()) {
					String rk = r.getKey();
					String ck = c.getKey();
					// We only collect these to get consistent error messages
					String first = rk;
					String second = ck;
					if (second.compareTo(first) < 0) {
						String tmp = first;
						first = second;
						second = tmp;
					}
					if (c.getValue() == Order.NONE) {
						// see if we can figure it out based on all the other possible entries
						Order outcome = Order.NONE;
						for (Entry<String, Map<String, Order>> x : ordering.entrySet()) {
							String xk = x.getKey();
							if (xk.equals(rk) || xk.equals(ck)) // don't consider r & c
								continue;
							Order mine = Order.NONE;
							if (r.getValue().get(xk) == Order.AFTER && x.getValue().get(ck) == Order.AFTER)
								mine = Order.AFTER;
							else if (r.getValue().get(xk) == Order.BEFORE && x.getValue().get(ck) == Order.BEFORE)
								mine = Order.BEFORE;
							if (mine != Order.NONE) {
								if (outcome == Order.NONE) {
									outcome = mine;
								}
								else if (outcome != mine) {
									ret.add("Ordering between " + first + " and " + second + " is inconsistent");
								}
							}
						}
						if (outcome != Order.NONE) {
							r.getValue().put(ck, outcome);
							ordering.get(ck).put(rk, outcome.invert());
							nchanged++;
						} else
							none.add("there is no ordering between " + first + " and " + second);
					} else if (c.getValue() == Order.INCONSISTENT) {
						ret.add("ordering between " + first + " and " + second + " is inconsistent");
					}
				}
			}
			if (nchanged == 0)
				break;
		}
		ret.addAll(none);
	}

	public List<String> bestOrdering(Set<String> errors) {
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
				} else if (mc == cnt) {
					errors.add("there is a cycle between " + best + " and " + r.getKey());
				}
			}
			if (cnt == -1)
				throw new RuntimeException("That should not be possible");
			ret.add(best);
		}
		if (ret.isEmpty() && haveDefault)
			ret.add("");
		return ret;
	}

	private int countBefores(Collection<Order> collection) {
		int ret = 0;
		for (Order o : collection)
			if (o == Order.AFTER)
				ret++;
		return ret;
	}
	
	public void dump() {
		for (Entry<String, Map<String, Order>> r : ordering.entrySet()) {
			System.out.println(r.getKey() + ": " + r.getValue());
		}
	}
}