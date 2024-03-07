package pl.edu.s28201.webExpenses.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.service.PasswordEncoderService;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    private PasswordEncoderService passwordService;

    @Autowired
    public WebSecurityConfig(PasswordEncoderService service) {
        passwordService = service;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**").permitAll();
                    auth.requestMatchers("/", "/landing").permitAll().anyRequest().authenticated();
                })
                .formLogin(form -> form.loginPage("/login").permitAll())
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUserDetails(new AppUser("email1@example.com", "John", "Doe", passwordService.encode("password1"))).build();

        log.info(user.getPassword());

        return new InMemoryUserDetailsManager(user);
    }
}
