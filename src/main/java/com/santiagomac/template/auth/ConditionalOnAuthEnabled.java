package com.santiagomac.template.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Registers the annotated bean only when app.auth.enabled=true (APP_AUTH_ENABLED). */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(prefix = "app.auth", name = "enabled", havingValue = "true")
public @interface ConditionalOnAuthEnabled {
}
