package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Set;

/** The purpose of a TestGroup is to enable us to run multiple sets of tests that
 * map to the same bar in (for example) different project contexts.
 * 
 * Thus the name is the name of the context in which the test is being run; inside
 * Eclipse, for example, that would most naturally be the name of the Project which
 * contained this set of tests.
 */
public interface TestResultGroup extends Comparable<TestResultGroup> {
	String name();
	Set<TestResultClass> testClasses();
	void add(TestInfo test);
}
