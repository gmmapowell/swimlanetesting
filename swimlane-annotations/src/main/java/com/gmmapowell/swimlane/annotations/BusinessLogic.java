package com.gmmapowell.swimlane.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The BusinessLogic annotation says this is a test of a core (adapter-free) business logic
 * 
 * If there are multiple hexagons, you must specify the hexagon being used
 * 
 * @author Gareth Powell
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BusinessLogic {
	Class<?> value();
	Class<?> hexagon() default Object.class;
}
