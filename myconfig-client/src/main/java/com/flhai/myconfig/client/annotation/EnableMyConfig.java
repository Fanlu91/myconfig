package com.flhai.myconfig.client.annotation;

import com.flhai.myconfig.client.config.MyConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * my config client entrypoint
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({MyConfigRegistrar.class})
public @interface EnableMyConfig {
}
