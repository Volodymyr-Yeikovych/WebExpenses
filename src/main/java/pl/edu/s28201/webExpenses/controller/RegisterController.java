package pl.edu.s28201.webExpenses.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.s28201.webExpenses.exception.AppUserAlreadyExistsException;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.dto.AppUserDto;
import pl.edu.s28201.webExpenses.service.AppUserService;
import pl.edu.s28201.webExpenses.service.SecurityService;

@Controller
@Slf4j
@RequestMapping("/register")
public class RegisterController {

    private final AppUserService userService;
    private final SecurityService securityService;

    @Autowired
    public RegisterController(AppUserService appUserService, SecurityService securityService) {
        this.userService = appUserService;
        this.securityService = securityService;
    }

    @GetMapping
    public String displayRegisterPage(Model model) {
        log.info("GET: /register");
        model.addAttribute("user", new AppUserDto());
        return "register";
    }

    @PostMapping
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid AppUserDto userDto,
            Errors errors,
            HttpServletRequest request,
            ModelAndView mav
    ) {
        log.info("POST: /register");

        if (errors.hasErrors()) {
            log.info("Errors: {}", errors.getAllErrors());
            return mav;
        }

        AppUser user;
        try {
            user = userService.registerNewUserAccount(userDto);
        } catch (AppUserAlreadyExistsException e) {
            log.info("User already exists.");
            mav.addObject("message", "An account for that email already exists.");
            return mav;
        }

        securityService.authenticateRegisteredUser(user, request);

        return new ModelAndView("expenses", "username", userService.getUsernameFromUser(user));
    }
}
