package org.gw.commons.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface LoggedMethod {
	
	public enum LEVEL { INFO, DEBUG}

	LEVEL level() default LEVEL.DEBUG;
	boolean timed() default true;
	int maxTimeInMillis() default 10;

}
