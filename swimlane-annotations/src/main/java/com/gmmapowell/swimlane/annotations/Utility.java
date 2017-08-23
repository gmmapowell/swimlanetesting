package com.gmmapowell.swimlane.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The Utility annotation says that this is a test of some portion of the system that is utility in nature; i.e. not associated with the main port/adapter model
 * 
 * @author Gareth Powell
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Utility {
	boolean autobuild() default false;
}
