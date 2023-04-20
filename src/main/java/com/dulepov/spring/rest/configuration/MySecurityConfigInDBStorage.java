package com.dulepov.spring.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class MySecurityConfigInDBStorage {

    @Autowired  //бин из myconfig
    DataSource dataSource;


    //бин для аутентификации (Basic auth через username and password-для запроса из postman нужно добавить эти credentials)
    //credentials хранятся в БД
    //проверка проиходит автоматически спрингом, если в БД будут записи о пользователях и ролях
    //в точном соответствии с entity user,role (см. туда)
    // (иначе проверку паролей/логинов придется писать самостоятельно)
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){        //по умолчанию использует BCryptPasswordEncoder??
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