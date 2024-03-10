package pl.edu.s28201.webExpenses.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;
//import pl.edu.s28201.webExpenses.service.PasswordEncoderService;

import java.util.Optional;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    public LoginController() {
    }

    @GetMapping
    public String displayLoginPage() {
        log.info("GET: Inside displayLoginPage()");
        return "login2";
    }

}
