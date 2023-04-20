package com.dulepov.spring.rest.configuration;

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
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//для теста MySecurityConfigInDBStorage
//@Configuration
//@EnableWebSecurity
public class MySecurityConfigInMemory {

    //бин, переопределящий базовый password encoder на BCrypt (защита лучше чем MD5 и SHA)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //бин для аутентификации (Basic auth через username and password-для запроса из postman нужно добавить эти credentials)
    //в целом это шляпа, так как данные аутентификации захардкожены и хранятся в памяти, а не в БД
    @Bean
    public UserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user"))   //кодирование пароля при записи в память
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))      //кодирование пароля при записи в память
                .roles("ADMIN")
                .build();


        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(user);
        inMemoryUserDetailsManager.createUser(admin);

        return inMemoryUserDetailsManager;
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