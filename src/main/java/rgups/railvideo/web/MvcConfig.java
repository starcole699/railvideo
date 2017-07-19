package rgups.railvideo.web;

import org.jminix.console.servlet.MiniConsoleServlet;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Dmitry on 04.07.2017.
 */
@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean ret = new MBeanServerFactoryBean();
        return ret;
    }


    @Bean
    public MiniConsoleServlet miniConsoleServlet() {
        return new MiniConsoleServlet();
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(MiniConsoleServlet miniConsoleServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(miniConsoleServlet, "/jmx/*");
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }
}