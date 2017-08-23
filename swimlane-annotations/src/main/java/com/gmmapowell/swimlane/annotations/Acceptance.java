package com.gmmapowell.swimlane.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The Acceptance annotation says that this is an acceptance test.
 * 
 * If the system involves multiple hexagons, then the default is that
 * it covers all of them (without specifying an ordering).  If an ordering is desired,
 * then one or more acceptance tests must specify a consistent total ordering.
 *
 * @author Gareth Powell
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Acceptance {
	/** An (ordered) set of class names representing the hexagons involved in the acceptance test
	 * 
	 * @return the (ordered) set of class names representing the hexagons
	 */
	Class<?>[] value() default {};
	
	/** In general, acceptance tests do not want to be automatically run at build time, because
	 * they are slow are resource-intensive.
	 * 
	 * However, if an acceptance test can be run very quickly (it only uses in-memory objects) this
	 * attribute can be set to true to enable autorunning on every build.
	 * 
	 * @return true if the test should be automatically run after a build; false if it should only be run on demand
	 */
	boolean autobuild() default false;
	
	/** If this acceptance test has been written, but the code has not been implemented,
	 * use this flag to "disable" it
	 * 
	 * @return true if the test in under active development; false if it is part of the regression suite
	 */
	boolean inprogress() default false;
}
