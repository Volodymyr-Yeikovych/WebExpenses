package pl.edu.s28201.webExpenses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.edu.s28201.webExpenses.config.format.LocalDateTimeFormatter;


@Configuration
@EnableWebMvc
public class WebConfig {

    @Bean
    public FormatterRegistry addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateTimeFormatter());
        return registry;
    }
}
