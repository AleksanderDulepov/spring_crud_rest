package com.dulepov.spring.rest.controller;


import com.dulepov.spring.rest.entity.Employee;
import com.dulepov.spring.rest.exception_handling.NoSuchEmployeeException;
import com.dulepov.spring.rest.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRESTController {

    //вызываем Service как компонент в spring контейнере (в данном случае через интерфейс, но можно и класс EmployeeServiceImpl)
    @Autowired
    private EmployeeService employeeService;

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
    public Employee updateEmployee(@PathVariable int empId, @RequestBody Employee employee){    //добавить @Valid из create!!!!!

        //проверка существования работника
        Employee emp=employeeService.getCurrentEmployee(empId);
        if (emp==null){
            throw new NoSuchEmployeeException("Работника с id="+empId+" не существует");	//см. класс обработки ошибок GlobalExceptionHandler
        }
        employee.setId(empId);
        employeeService.saveEmployee(employee);
        return employee;

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
