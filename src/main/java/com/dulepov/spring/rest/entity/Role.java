package com.dulepov.spring.rest.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authorities")
public class Role implements Serializable {


    //для проверки springом credentials самостоятельно

//    CREATE TABLE authorities (
//            username varchar(15),
//    authority varchar(25),
//    FOREIGN KEY (username) references users(username)
//            ) ;

//    INSERT INTO my_db.authorities (username, authority)
//    VALUES
//            ('alex', 'ROLE_ADMIN'),
//    ('alex', 'ROLE_USER'),
//            ('enemy', 'ROLE_USER');

    @Id //multiple PK
    //many-to-one потому что Role мб много для одного User
    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name="username")    //это название столбца (В БД), который мы называем сами, где будут хранится ссылки на User
    private User username;  //переменную в entity аналогично можем назвать как угодно, но она будет содержать FK на обьект User

    @Id //multiple PK
    @Column(name = "authority")
    private String authority;



}
