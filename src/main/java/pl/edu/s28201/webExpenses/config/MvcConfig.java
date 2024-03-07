package pl.edu.s28201.webExpenses.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/landing").setViewName("landing");
        registry.addViewController("/").setViewName("landing");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login2");
        registry.addViewController("/expenses").setViewName("expenses");
    }
}
