package com.gmmapowell.swimlane.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The Adapter annotation says this is a test of an adapter.
 * 
 * To bind adapters to ports and ports to hexagons at least one such test must specify the hexagon and port.
 * If you want to place the port in a specific location relative to the hexagon, specify the location property
 * 
 * @author Gareth Powell
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Adapter {
	Class<?> value();
	Class<?> hexagon() default Object.class;
	Class<?> port() default Object.class;
	Location location() default Location.NONE;
	boolean autobuild() default false;
}
