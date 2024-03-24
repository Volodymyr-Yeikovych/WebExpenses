package pl.edu.s28201.webExpenses.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomLogoutHandler logoutHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, CustomLogoutHandler logoutHandler) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/webjars/**").permitAll()
                                .requestMatchers("/", "/landing", "/login**", "/error/**", "/callback/").permitAll()
                                .anyRequest().authenticated())
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer.logoutUrl("/logout")
                                .addLogoutHandler(logoutHandler)
//                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                                .logoutSuccessUrl("/login?logout")
                                .permitAll())
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error"));

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
