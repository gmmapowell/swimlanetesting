package com.gmmapowell.swimlane.eclipse.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
			boolean foundSomething = false;
			Class<?> tc = Class.forName(clzName, false, cl);
			for (Annotation y : tc.getAnnotations()) {
				String aname = y.annotationType().getName();
				if (aname.equals("com.gmmapowell.swimlane.annotations.Acceptance")) {
					try {
						Class<?>[] hexes = (Class<?>[]) y.getClass().getMethod("value").invoke(y);
						accumulator.acceptance(grp, tc, Arrays.asList(hexes));
						foundSomething = true;
					} catch (Exception e) {
						accumulator.error(e.getMessage());
					}
				} else if (aname.equals("com.gmmapowell.swimlane.annotations.BusinessLogic")) {
					try {
						Class<?> hex = null;
						Class<?> tmp = (Class<?>) y.getClass().getMethod("value").invoke(y);
						if (!tmp.equals(Object.class))
							hex = tmp;
						accumulator.logic(grp, tc, hex);
						foundSomething = true;
					} catch (Exception e) {
						accumulator.error(e.getMessage());
					}
				}
			}
			if (!foundSomething) {
				for (Method m : tc.getMethods()) {
					for (Annotation y : m.getAnnotations())
						if (y.annotationType().getName().equals("org.junit.Test")) {
							accumulator.error(clzName + " has @Test annotations but no swimlane annotations");
						}
				}
			}
		} catch (ClassNotFoundException e1) {
			accumulator.error("No such class: " + e1.getMessage());
		}
	}

}
