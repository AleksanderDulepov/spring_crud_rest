package com.dulepov.spring.rest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckEmailValidator.class)
public @interface CheckEmail {

    public String value() default "yandex.com";
    public String message() default "Почта должна заканчиваться на yandex.com";

    //технологический блок(just paste)
    public Class<?>[] groups() default {};	//позволяет программистам разбивать аннотации по группам
    public Class<? extends Payload>[] payload() default {};		//для переноса информации метаданных клиента


}
