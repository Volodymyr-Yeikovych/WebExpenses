package pl.edu.s28201.webExpenses.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.SecurityAppUser;

@Service
public class SecurityService {

    public AppUser getUserFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityAppUser securityAppUser = (SecurityAppUser) authentication.getPrincipal();
        return securityAppUser.getUser();
    }
}
