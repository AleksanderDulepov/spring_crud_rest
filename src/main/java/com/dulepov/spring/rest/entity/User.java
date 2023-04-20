package com.dulepov.spring.rest.entity;

import jakarta.persistence.*;

import java.util.List;

//для проверки springом credentials самостоятельно
//USE my_db;
//
//        CREATE TABLE users (
//        username varchar(15),
//        password varchar(100),
//        enabled tinyint(1),
//        PRIMARY KEY (username)
//        ) ;

//INSERT INTO my_db.users (username, password, enabled)
//        VALUES
//        ('alex', '{noop}alex', 1),
//        ('enemy', '{noop}enemy', 1);

        //{noop} - тип шифрования
        //enabled 1-активный пользователь, 0-неактивный

@Entity
@Table(name = "users")
public class User {


    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;


    @Column(name = "enabled", columnDefinition = "TINYINT")
    private boolean enabled;    //вот тут большое ХЗ, может какой нибудь int. Проверка CRUDом все покажет

    //Это только для entity, в БД такого поля не будет!
    @OneToMany(mappedBy="username",     //поиск среди полей класса Role поля с m2o связью на User(в данном случае ПОЛЕ username)
                cascade={CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
    //one-to-many потому что User один для многих Role
    private List<Role> roles;   //List т.к множество обьектов


    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
