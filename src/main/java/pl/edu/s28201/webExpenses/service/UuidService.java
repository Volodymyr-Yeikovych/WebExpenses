package pl.edu.s28201.webExpenses.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UuidService {

    public List<UUID> parseExpenseIds(String string, String separator) {
        List<UUID> uuids = new ArrayList<>();
        for (String s : string.split(separator)) {
            try {
                UUID uuid = UUID.fromString(s);
                uuids.add(uuid);
            } catch (IllegalArgumentException ignored) {}
        }
        return uuids;
    }
}
