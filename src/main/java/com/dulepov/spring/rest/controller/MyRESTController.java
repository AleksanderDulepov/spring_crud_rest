package com.dulepov.spring.rest.controller;


import com.dulepov.spring.rest.entity.Employee;
import com.dulepov.spring.rest.exception_handling.NoSuchEmployeeException;
import com.dulepov.spring.rest.service.CommonService;
import com.dulepov.spring.rest.service.EmployeeService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MyRESTController {

    //вызываем Service как компонент в spring контейнере (в данном случае через интерфейс, но можно и класс EmployeeServiceImpl)
    @Autowired
    private EmployeeService employeeService;

    //вызываем Service как компонент в spring контейнере
    @Autowired
    private CommonService commonService;

    //READ ALL
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        List<Employee> emps = employeeService.getAllEmployees();
        return emps;
    }

    //READ ONE
    @GetMapping("/employees/{empId}")
    public Employee getCurrentEmployee(@PathVariable int empId) {    //получение значения из сегмента url

        Employee emp = employeeService.getCurrentEmployee(empId);
        //проверка нулевого результата(если записи с таким id не нашлось)
        if (emp == null) {
            throw new NoSuchEmployeeException("Работника с id=" + empId + " не существует");    //см. класс обработки ошибок GlobalExceptionHandler
        }

        return emp;
    }

    //CREATE
    @PostMapping("/employees")	//работает только с POST-запросом
    public Employee addNewEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {

        //проверка валидации
        if (bindingResult.hasErrors()){
            //получение ошибок валидации
            List<FieldError> validErrors=bindingResult.getFieldErrors();
            List<String> errorsDescList = new ArrayList<>();

            for (FieldError error:validErrors){
                errorsDescList.add(error.getField()+" - "+error.getDefaultMessage());
            }

            throw new ValidationException("Ошибки валидации для следующих полей: "+String.join(", ", errorsDescList));
        }

        //в теле не прописываем id, иначе если он будет то произойдет попытка обновить пользователя, см.EmployeeDAOImpl.saveEmployee
        //сохраняем уже сериализованный объект в базу
        employeeService.saveEmployee(employee);

        //выводим в response, но уже сохраненный вариант из базы (c id)
        return employee;
    }

    //UPDATE
    @PutMapping("/employees/{empId}")	//работает только с PUT-запросом
    public Employee updateEmployee(@PathVariable int empId, @Valid @RequestBody Employee employee, BindingResult bindingResult){

        //проверка валидации
        if (bindingResult.hasErrors()){
            //получение ошибок валидации
            List<FieldError> validErrors=bindingResult.getFieldErrors();
            List<String> errorsDescList = new ArrayList<>();

            for (FieldError error:validErrors){
                errorsDescList.add(error.getField()+" - "+error.getDefaultMessage());
            }

            throw new ValidationException("Ошибки валидации для следующих полей: "+String.join(", ", errorsDescList));
        }


        //проверка существования работника
        Employee emp=employeeService.getCurrentEmployee(empId);
        if (emp==null){
            throw new NoSuchEmployeeException("Работника с id="+empId+" не существует");	//см. класс обработки ошибок GlobalExceptionHandler
        }
        employee.setId(empId);
        employeeService.saveEmployee(employee);
        return employee;

    }


    //PARTIAL UPDATE
    @PatchMapping(path="/employees/{empId}")	//работает только с PATCH-запросом
    public ResponseEntity<Employee> partialUpdateEmployee(@PathVariable int empId, @RequestBody JsonMergePatch patchJson) {

        //проверка существования работника
        Employee emp=employeeService.getCurrentEmployee(empId);
        if (emp==null){
            throw new NoSuchEmployeeException("Работника с id="+empId+" не существует");	//см. класс обработки ошибок GlobalExceptionHandler
        }

        //применяем частичное обновление-изменяем обьект по переданному json
        Employee empPatched=commonService.applyPatchToEmployee(patchJson, emp, Employee.class);

        //если в json будет передан id, то будет происходить попытка его обновить и возникнет ошибка
        //чтобы этого избежать либо добавить кастомную валидацию на отсутствие id
        //либо после перезаписи на переданный id повторно присвоить ему изначальный id приудительно
        empPatched.setId(empId);

        //принудительная валидация
        Validator validator;
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        validator=validatorFactory.getValidator();

        Set<ConstraintViolation<Employee>> validationResultSet= validator.validate(empPatched);

        if (validationResultSet.size()>0){
            List<String> errorsDescList = new ArrayList<>();

            for (ConstraintViolation<Employee> item:validationResultSet){
                errorsDescList.add(item.getPropertyPath().toString()+" - "+item.getMessage());
            }
            throw new ValidationException("Ошибки валидации для следующих полей: "+String.join(", ", errorsDescList));
        }


        //сохраняем обьект в базу
        employeeService.saveEmployee(empPatched);

        return ResponseEntity.ok(empPatched);

    }



    //DELETE
    @DeleteMapping("/employees/{empId}")	//работает только с DELETE-запросом
    public ResponseEntity<String> deleteEmployee(@PathVariable int empId){

        //проверка существования работника
        Employee emp=employeeService.getCurrentEmployee(empId);
        if (emp==null){
            throw new NoSuchEmployeeException("Работника с id="+empId+" не существует");	//см. класс обработки ошибок GlobalExceptionHandler
        }

        employeeService.deleteEmployee(empId);

        //возвращаем результат удаления строкой

        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");
        return new ResponseEntity<>("Работник с id="+empId+" успешно удален", h, HttpStatus.OK);

    }


}
