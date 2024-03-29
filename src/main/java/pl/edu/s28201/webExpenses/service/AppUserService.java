package pl.edu.s28201.webExpenses.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.exception.AppUserAlreadyExistsException;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.dto.AppUserDto;
import pl.edu.s28201.webExpenses.repository.AppUserRepository;

@Service
@Transactional
@Slf4j
public class AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;
    private final DefaultUserDataCreationService dataCreationService;
    @Autowired
    public AppUserService(AppUserRepository repository, PasswordEncoder encoder, DefaultUserDataCreationService dataCreationService) {
        this.repository = repository;
        this.encoder = encoder;
        this.dataCreationService = dataCreationService;
    }

    public AppUser registerNewUserAccount(AppUserDto dto) throws AppUserAlreadyExistsException {
        if (emailExists(dto.getEmail())) {
            throw new AppUserAlreadyExistsException("User with given email [" + dto.getEmail() + "] already exists!");
        }

        AppUser user = AppUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();

        repository.save(user);

        dataCreationService.addDefaultDataToUser(user);

        log.info("Got from Registration: " + user);

        return user;
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public String getUsernameFromUser(AppUserDto userDto) {
        return getUsername(userDto.getFirstName(), userDto.getLastName());
    }

    public String getUsernameFromUser(AppUser user) {
        return getUsername(user.getFirstName(), user.getLastName());
    }

    private String getUsername(String firstName, String lastName) {
        StringBuilder username = new StringBuilder(firstName);

        if (!lastName.isEmpty()) {
            username.append(" ").append(lastName);
        }

        return username.toString();
    }
}
