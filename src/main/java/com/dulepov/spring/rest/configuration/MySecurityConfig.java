package com.dulepov.spring.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


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
        http.authorizeRequests()
                .requestMatchers(HttpMethod.GET,"/employees").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET,"/employees/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"/employees").hasRole("ADMIN")
                .and().formLogin().permitAll();
        return http.build();
    }


}