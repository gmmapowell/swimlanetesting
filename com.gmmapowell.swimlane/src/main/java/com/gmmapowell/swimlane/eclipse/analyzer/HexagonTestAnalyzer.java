package com.gmmapowell.swimlane.eclipse.analyzer;

import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.Arrays;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ClassAnalyzer;

public class HexagonTestAnalyzer implements ClassAnalyzer {
	private final URLClassLoader cl;
	private final Accumulator accumulator;

	public HexagonTestAnalyzer(URLClassLoader cl, Accumulator accumulator) {
		this.cl = cl;
		this.accumulator = accumulator;
	}

	@Override
	public void consider(String clzName) {
		try {
			Class<?> tc = Class.forName(clzName, false, cl);
			for (Annotation y : tc.getAnnotations()) {
				if (y.annotationType().getName().equals("com.gmmapowell.swimlane.annotations.Acceptance")) {
					try {
						Class<?>[] hexes = (Class<?>[]) y.getClass().getMethod("value").invoke(y);
						accumulator.acceptance(tc, Arrays.asList(hexes));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e1) {
			accumulator.classError("No such class: " + e1.getMessage());
		}
	}

}
