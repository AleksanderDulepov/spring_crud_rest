package com.dulepov.spring.rest.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//реализация Dispatcher Servlet

public class MyWebInitilizer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {

        return new Class[] {MyConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }   //общий сегмент url
}
