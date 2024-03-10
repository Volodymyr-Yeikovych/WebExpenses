package pl.edu.s28201.webExpenses.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.SecurityAppUser;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public JpaUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(SecurityAppUser::new).orElseThrow(() -> new UsernameNotFoundException("Email {" + email + "} not found."));
    }
}
