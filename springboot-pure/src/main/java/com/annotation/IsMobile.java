package com.annotation;


import com.validator.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { IsMobileValidator.class})
public @interface IsMobile {

    boolean required() default true;//是否校验

    String message() default "手机号码错误"; // 约束注解验证时的输出消息

    Class<?>[] groups() default { };// 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default { };// 约束注解的有效负载
}

