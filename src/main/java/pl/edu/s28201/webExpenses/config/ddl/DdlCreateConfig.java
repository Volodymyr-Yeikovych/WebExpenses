package pl.edu.s28201.webExpenses.config.ddl;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

@Profile("ddl-create")
@Configuration
public class DdlCreateConfig implements DdlAutoConfig{
    @Override
    public Properties getDdlConfig() {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        return jpaProperties;
    }
}
