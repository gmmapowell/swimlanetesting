package com.gmmapowell.swimlane.eclipse.analyzer;

import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.Arrays;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ClassAnalyzer;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class HexagonTestAnalyzer implements ClassAnalyzer {
	private final TestGroup grp;
	private final URLClassLoader cl;
	private final Accumulator accumulator;

	public HexagonTestAnalyzer(TestGroup grp, URLClassLoader cl, Accumulator accumulator) {
		this.grp = grp;
		this.cl = cl;
		this.accumulator = accumulator;
	}

	@Override
	public void consider(String clzName) {
		try {
			Class<?> tc = Class.forName(clzName, false, cl);
			for (Annotation y : tc.getAnnotations()) {
				String aname = y.annotationType().getName();
				if (aname.equals("com.gmmapowell.swimlane.annotations.Acceptance")) {
					try {
						Class<?>[] hexes = (Class<?>[]) y.getClass().getMethod("value").invoke(y);
						accumulator.acceptance(grp, tc, Arrays.asList(hexes));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (aname.equals("com.gmmapowell.swimlane.annotations.BusinessLogic")) {
					try {
						Class<?> hex = null;
						Class<?> tmp = (Class<?>) y.getClass().getMethod("value").invoke(y);
						if (!tmp.equals(Object.class))
							hex = tmp;
						accumulator.logic(grp, tc, hex);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e1) {
			accumulator.error("No such class: " + e1.getMessage());
		}
	}

}
