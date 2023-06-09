package com.dulepov.spring.rest.service;

import com.dulepov.spring.rest.entity.Employee;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CommonService {

    //PARTIAL UPDATE UNIVERSAL
    public <T> T applyPatchToEmployee(JsonMergePatch patchJson, T target, Class<T> targetClass);

    //обработка ошибок валидации
    public void parseValidationResults(BindingResult bindingResult);


}
