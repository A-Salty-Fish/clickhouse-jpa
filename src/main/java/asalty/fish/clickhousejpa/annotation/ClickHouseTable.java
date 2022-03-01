package asalty.fish.clickhousejpa.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author 13090
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickHouseTable {
    /**
     * @return the name of the table
     */
    String name();

    /**
     * @return other table information
     */
    String otherInfo() default "";

    ClickHouseEngine engine() default ClickHouseEngine.MergeTree;
}

