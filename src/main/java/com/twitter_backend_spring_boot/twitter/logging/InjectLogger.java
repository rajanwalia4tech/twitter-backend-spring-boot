package com.twitter_backend_spring_boot.twitter.logging;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectLogger {
}
