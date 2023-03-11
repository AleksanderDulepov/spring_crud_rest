package com.dulepov.spring.rest.service;

import com.dulepov.spring.rest.dao.EmployeeDAO;
import com.dulepov.spring.rest.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class EmployeeServiceImpl implements EmployeeService {
    //вызываем DAO как компонент в spring контейнере (в данном случае через интерфейс, но можно и класс EmployeeDAOImpl)
    //чтобы не создавать  новый обьект EmployeeDAOImpl
    @Autowired
    private EmployeeDAO employeeDAO;

    //READ
    @Override
    @Transactional
    public List<Employee> getAllEmployees(){
        List<Employee> emps=employeeDAO.getAllEmployees();
        return emps;
    }

    //CREATE
    @Override
    @Transactional
    public void saveEmployee(Employee emp){
        employeeDAO.saveEmployee(emp);
    }

    //UPDATE
    @Override
    @Transactional
    public Employee getCurrentEmployee(int id){
        Employee emp=employeeDAO.getCurrentEmployee(id);
        return emp;
    }

    //DELETE
    @Override
    @Transactional
    public void deleteEmployee(int id){
        employeeDAO.deleteEmployee(id);
    }

}
