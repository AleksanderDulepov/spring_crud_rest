package com.dulepov.spring.rest.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.dulepov.spring.rest")    //Меняем на название пакеты под main/java
@EnableWebMvc
@EnableTransactionManagement
public class MyConfig {

    @Bean
    public DataSource dataSource(){
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/my_db?useSSL=false" +
                "&amp&serverTimezone=UTC" +
                "&allowPublicKeyRetrieval=true" +
                "&createDatabaseIfNotExist=true");
        dataSource.setUser("bestuser");     //Логин и пароль для доступа в БД(был создан отдельный дев.юзер для доступа))
        dataSource.setPassword("bestuser");

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactory=new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.dulepov.spring.rest.entity");  //Адрес пакета с entity

        Properties prop=new Properties();
        prop.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.hbm2ddl.auto","update");    //Возможность создавать таблицы по entity (без предварительного создания их прямо в БД)

        sessionFactory.setHibernateProperties(prop);

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager(){
        HibernateTransactionManager hibernateTransactionManager=new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
        return hibernateTransactionManager;
    }




}
