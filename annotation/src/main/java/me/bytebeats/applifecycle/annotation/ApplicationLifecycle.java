package me.bytebeats.applifecycle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by bytebeats on 2021/6/1 : 15:10
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ApplicationLifecycle {
}
