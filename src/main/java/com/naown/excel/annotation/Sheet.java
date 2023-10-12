package com.naown.excel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @date 2021/4/29 15:03
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

    int sheetNo() default 1;

    /**
     * sheet name
     */
    String sheetName();

    /**
     * 包含字段
     */
    String[] includes() default {};

    /**
     * 排除字段
     */
    String[] excludes() default {};

    /**
     * 头生成器
     */
    //Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
