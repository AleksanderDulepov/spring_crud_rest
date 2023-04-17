package com.dulepov.spring.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


//в целом это шляпа, так как данные аутентификации захардкожены
//хранение в паролей в БД будет далее

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    //бин для аутентификации (Basic auth через username and password-для запроса из postman нужно добавить эти credentials)
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        InMemoryUserDetailsManager inMemoryUserDetailsManager=new InMemoryUserDetailsManager();
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
                .requestMatchers(HttpMethod.GET,"/api/employees").permitAll()  //разрешает доступ всем, даже не аутентифицированным
                .requestMatchers(HttpMethod.GET,"/api/employees/**").authenticated()//разрешает доступ всем ролям, но только аутентифифированным
                .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/api/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN");


        return http.build();
    }




}