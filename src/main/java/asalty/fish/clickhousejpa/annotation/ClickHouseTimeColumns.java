package asalty.fish.clickhousejpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickHouseTimeColumns {
    String value() default "";

    boolean year() default true;

    boolean month() default true;

    boolean day() default true;
}
