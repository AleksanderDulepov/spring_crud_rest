package com.dulepov.spring.rest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.executable.ValidateOnExecution;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

public class CheckEmailValidator implements ConstraintValidator <CheckEmail, String>{


    private String endOfEmail;

    @Override
    public void initialize(CheckEmail checkEmail) {

        endOfEmail=checkEmail.value();
    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext constraintValidatorContext) {

            //если поле вообще не было передано
            if (inputValue==null){
                return false;
            }

            return inputValue.endsWith(endOfEmail);

    }

}
