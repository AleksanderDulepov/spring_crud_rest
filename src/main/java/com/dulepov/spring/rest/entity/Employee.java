package com.dulepov.spring.rest.entity;


import com.dulepov.spring.rest.validation.CheckEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")  //из БД
    @NotEmpty(message="Это поле обязательно для заполнения")   //для валидации
    private String name;    //так будет в json

    @Column(name = "surname")
    @NotEmpty(message="Это поле обязательно для заполнения")
    private String surname;

    @Column(name = "department")
    private String department;

    @Column(name = "salary")
    private int salary;

    @Column(name = "email")
    @CheckEmail(value = "mail.ru", message="Почта должна заканчиваться на mail.ru")    //кастомный валидатор
    private String email;

    @Column(name = "phone")
    @Pattern(regexp="\\d{3}-\\d{2}-\\d{2}", message="please use pattern XXX-XX-XX")
    private String phoneNumber;


    public Employee() {
    }

    public Employee(int id, String name, String surname, String department, int salary, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.salary = salary;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

