package com.dulepov.spring.rest.service;

import com.dulepov.spring.rest.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public List<Employee> getAllEmployees();    //READ

    public void saveEmployee(Employee emp);        //CREATE

    public Employee getCurrentEmployee(int id);    //UPDATE

    public void deleteEmployee(int id);    //DELETE

}
