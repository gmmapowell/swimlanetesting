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
}
