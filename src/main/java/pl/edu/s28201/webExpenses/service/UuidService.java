package pl.edu.s28201.webExpenses.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UuidService {

    public List<UUID> parseExpenseIds(String string, String separator) {
        return Arrays.stream(string.split(separator)).map(UUID::fromString).toList();
    }
}
