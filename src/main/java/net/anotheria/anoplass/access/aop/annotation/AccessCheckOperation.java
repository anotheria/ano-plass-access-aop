package net.anotheria.anoplass.access.aop.annotation;

import net.anotheria.anoplass.access.aop.AccessCheckedAPI;
import net.anotheria.anoplass.api.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark all methods to check if given operation can be applied to given object.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessCheckOperation {

    /**
     * Operation to be applied.
     */
    AccessCheckAction action();

    /**
     * API's class for access check.
     */
    Class<? extends AccessCheckedAPI> accessApi();
}
