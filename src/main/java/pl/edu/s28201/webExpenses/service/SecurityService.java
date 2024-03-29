package pl.edu.s28201.webExpenses.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.SecurityAppUser;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@Slf4j
public class SecurityService {

    private final AuthenticationManager authManager;

    @Autowired
    public SecurityService(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public AppUser getUserFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityAppUser securityAppUser = (SecurityAppUser) authentication.getPrincipal();
        return securityAppUser.getUser();
    }

    public void authenticateRegisteredUser(AppUser user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user, user.getPassword());

        log.debug("Token: {}", authReq);

        authReq.setDetails(new WebAuthenticationDetails(request));
        Authentication auth = authManager.authenticate(authReq);

        log.debug("Token after Auth: {}", auth);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        log.debug("Security context: {}", sc);

        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }
}
