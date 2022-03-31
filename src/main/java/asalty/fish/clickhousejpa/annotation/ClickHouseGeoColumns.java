package asalty.fish.clickhousejpa.annotation;

public @interface ClickHouseGeoColumns {

    String value() default "";

    int level() default 3;
}
