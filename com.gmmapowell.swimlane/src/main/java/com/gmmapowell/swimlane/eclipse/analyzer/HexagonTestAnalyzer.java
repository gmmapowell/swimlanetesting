package com.gmmapowell.swimlane.eclipse.analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ProjectAnalyzer;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRole;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;
import com.gmmapowell.swimlane.eclipse.roles.UnlabelledTestRole;

public class HexagonTestAnalyzer implements ProjectAnalyzer {
	private final ErrorAccumulator eh;
	private final DataCentral hub;
	private AnalysisAccumulator accumulator;

	public HexagonTestAnalyzer(ErrorAccumulator eh, DataCentral hub) {
		this.eh = eh;
		this.hub = hub;
	}

	@Override
	public void startAnalysis(Date startTime) {
		accumulator = hub.startAnalysis(startTime);
	}
	
	@Override
	public void consider(GroupOfTests grp, URLClassLoader cl, String clzName) {
		try {
			Class<?> tc = Class.forName(clzName, false, cl);
			TestRole finalRole = null;
			for (Annotation sla : tc.getAnnotations()) {
				try {
					String aname = sla.annotationType().getName();
					TestRole role = null;
					if (aname.equals("com.gmmapowell.swimlane.annotations.Acceptance")) {
						Class<?>[] hexes = (Class<?>[]) sla.getClass().getMethod("value").invoke(sla);
						role = new AcceptanceRole(hexes);
					} else if (aname.equals("com.gmmapowell.swimlane.annotations.BusinessLogic")) {
						Class<?> hex = null;
						Class<?> tmp = (Class<?>) sla.getClass().getMethod("value").invoke(sla);
						if (!tmp.equals(Object.class))
							hex = tmp;
						role = new BusinessRole(hex);
					} else if (aname.equals("com.gmmapowell.swimlane.annotations.Adapter")) {
						Class<?> adapter = (Class<?>) sla.getClass().getMethod("value").invoke(sla);
						Class<?> hex = null, port = null;
						PortLocation pl = null;
						{
							Class<?> tmp = (Class<?>) sla.getClass().getMethod("hexagon").invoke(sla);
							if (!tmp.equals(Object.class))
								hex = tmp;
						}
						{
							Class<?> tmp = (Class<?>) sla.getClass().getMethod("port").invoke(sla);
							if (!tmp.equals(Object.class))
								port = tmp;
						}
						{
							Object tmp = sla.getClass().getMethod("location").invoke(sla);
							String loc = tmp.toString();
							if (!"NONE".equals(loc)) {
								pl = PortLocation.valueOf(loc);
							}
						}
						role = new AdapterRole(hex, port, pl, adapter);
					} else if (aname.equals("com.gmmapowell.swimlane.annotations.Utility")) {
						role = new UtilityRole();
					}
					if (role != null && finalRole != null) {
						eh.error("cannot have multiple role annotations on " + clzName);
						return;
					}
					finalRole = role;
				} catch (Exception e) {
					eh.error(e.getMessage());
				}
			}
			List<String> tests = new ArrayList<>();
			for (Method m : tc.getMethods()) {
				for (Annotation ts : m.getAnnotations())
					if (ts.annotationType().getName().equals("org.junit.Test")) {
						tests.add(m.getName());
						if (finalRole == null)
							finalRole = new UnlabelledTestRole();
					}
			}
			if (finalRole != null)
				accumulator.haveTestClass(grp, clzName, finalRole, tests);
		} catch (ClassNotFoundException e1) {
			eh.error("No such class: " + e1.getMessage());
		}
	}

	@Override
	public void analysisComplete(Date completeTime) {
		accumulator.analysisComplete(completeTime);
	}
}
