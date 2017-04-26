package com.gmmapowell.swimlane.eclipse.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
			System.out.println("tc = " + tc);
			for (Method m : tc.getDeclaredMethods()) {
				System.out.println("meth = " + m);
				for (Annotation a : m.getDeclaredAnnotations())
					System.out.println("ma = " + a);
			}
			for (Annotation da : tc.getDeclaredAnnotations()) {
				System.out.println("m = " + da);
			}
			for (Annotation y : tc.getAnnotations()) {
				System.out.println("ann " + y + " " + y.annotationType().getName());
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
			e1.printStackTrace();
		}
	}

}
