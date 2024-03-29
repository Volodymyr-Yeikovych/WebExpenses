package pl.edu.s28201.webExpenses.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.SecurityAppUser;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

@Service
@Slf4j
public class AppUserAuthenticationManager implements AuthenticationManager{

    private final AppUserRepository userRepository;

    @Autowired
    public AppUserAuthenticationManager(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AppUser user = (AppUser) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        Optional<AppUser> userOpt = userRepository.findByEmail(user.getEmail());
        if (userOpt.isEmpty()) {
            log.info("Given User to Authenticate was not found.");
            throw new BadCredentialsException("1000");
        }
        AppUser userAuth = userOpt.get();
        if (!password.equals(userAuth.getPassword())) {
            log.info("Invalid Authentication password.");
            throw new BadCredentialsException("1000");
        }

        SecurityAppUser secUser = new SecurityAppUser(user);

        return new UsernamePasswordAuthenticationToken(secUser, password, secUser.getAuthorities());
    }
}