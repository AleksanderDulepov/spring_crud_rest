package com.dulepov.spring.rest.dao;

import com.dulepov.spring.rest.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO{

    //интегрируем доступ к sessionFactory через одноименный бин(см. aplicationContext)
    @Autowired
    private SessionFactory sessionFactory;

    //READ
    @Override
    @Transactional
    public List<Employee> getAllEmployees(){

        //получаем сессию
        Session session=sessionFactory.getCurrentSession();

        //получаем информацию из базы
        List<Employee> allEmployees=session.createQuery("from Employee",  Employee.class).getResultList();

        return allEmployees;
    }

    //CREATE
    @Override
    public void saveEmployee(Employee emp){

        //получаем сессию
        Session session=sessionFactory.getCurrentSession();

//        if (emp.getId==0){
//            session.save(emp);	//CREATE
//        } else {
//            session.update(emp)	//UPDATE
//                    [тут как-то по-другому апдейтится, смотри hibernate]
//        }

        //блок if-else лучше оформить так:
        session.saveOrUpdate(emp);


    }

    //UPDATE
    @Override
    public Employee getCurrentEmployee(int id){

        //получаем сессию
        Session session=sessionFactory.getCurrentSession();

        //получаем обьект
        Employee emp=session.get(Employee.class, id);

        return emp;
    }

    //DELETE
    @Override
    public void deleteEmployee(int id){

        //получаем сессию
        Session session=sessionFactory.getCurrentSession();

        //удаляем обьект
        Query<Employee> query=session.createQuery("delete from Employee where id=:employeeId");
        query.setParameter("employeeId", id);
        query.executeUpdate();
    }

}
