package com.dulepov.spring.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class MySecurityConfigInDBStorage {

    @Autowired  //бин из myconfig
    DataSource dataSource;


    /*
    бин для аутентификации (Basic auth через username and password-для запроса из postman нужно добавить эти credentials)
    credentials хранятся в БД
    проверка проиходит автоматически спрингом, если в БД будут записи о пользователях и ролях в точном соответствии
    с entity user,role (см. туда)
     (иначе проверку паролей/логинов придется писать самостоятельно)
     */

    /*
    При использовании встроенного механизма сверки credentials кодирование пароля осуществляется следующим образом:
    В БД users.password пароли лежат в виде {bcrypt} $2y$10$yQGLqzqs/05vCbs0BQD1ae6p2vtm4jv7h5uX4Z9dDyFSUcTKBxd4u
    bcrypt-тип шифрования, захешированный пароль (alex)
    При передачи пароля с клиента в БД по логину находится пароль, считывается bcrypt, соль из хеша, введенный пароль с солью
    кодируется по bcrypt, сверяется с хешем из БД

    Если не ок такой формат хранения данных в БД-надо писать свой сервис проверки, встраивать кодировщик
    Если нужен CRUD для создания пользователей-надо дополнитеьно хешировать пароль по bcrypt перед сохранением в БД
     */

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }


    //бин для авторизации
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic()    //basic-аутентификация
                .and()
                .csrf().disable()   //удаляет проверку csrf для методов put, post, patch, delete

                .authorizeHttpRequests()    //предоставить разрешения для следующих url
                .requestMatchers(HttpMethod.GET, "/api/employees").permitAll()  //разрешает доступ всем, даже не аутентифицированным
                .requestMatchers(HttpMethod.GET, "/api/employees/**").authenticated()//разрешает доступ всем ролям, но только аутентифифированным
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN");


        return http.build();
    }


}