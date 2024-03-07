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
import pl.edu.s28201.webExpenses.service.PasswordEncoderService;

import java.util.Optional;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private PasswordEncoderService passwordService;
    private AppUserRepository appUserRepository;

    @Autowired
    public LoginController(PasswordEncoderService passwordService, AppUserRepository appUserRepository) {
        this.passwordService = passwordService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public String displayLoginPage() {
        log.info("GET: Inside displayLoginPage()");
        return "login2";
    }

    @PostMapping
    public String redirectToExpensesPage(@Valid @ModelAttribute AppUser appUser, Errors errors, SessionStatus sessionStatus) {
        log.info("POST: Inside redirectToExpensesPage()");
        String email = appUser.getEmail();
        String password = appUser.getPassword();
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.info("AppUser with email {} was not found.", email);
            return "redirect:/login?error";
        }
        AppUser user = userOptional.get();
        String pswEncoded = user.getPassword();

        if (passwordService.validate(password, pswEncoded)) {
            log.info("AppUser email {} successfully logged in", email);
            return "redirect:/expenses";
        }
        log.info("Incorrect password for AppUser with email {}.", email);
        return "redirect:/login?error";
    }

    @ModelAttribute(name = "AppUser")
    public AppUser appUser() {
        return new AppUser();
    }
}
