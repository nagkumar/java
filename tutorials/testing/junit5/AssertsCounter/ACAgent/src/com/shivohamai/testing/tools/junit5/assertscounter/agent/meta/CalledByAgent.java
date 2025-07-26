package com.shivohamai.testing.tools.junit5.assertscounter.agent.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method or class as being invoked dynamically by a JVM agent (e.g., via Byte Buddy).
 * <p>
 * This annotation serves as a signal to static analysis tools and developers that the annotated
 * element is indeed used, even though there may be no direct source code references to it.
 * It helps prevent "unused" warnings from IDEs and clarifies the code's entry points.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface CalledByAgent
{
}