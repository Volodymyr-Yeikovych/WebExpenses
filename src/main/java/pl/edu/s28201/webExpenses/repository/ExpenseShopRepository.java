package pl.edu.s28201.webExpenses.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.ExpenseShop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseShopRepository extends CrudRepository<ExpenseShop, UUID> {

    Optional<ExpenseShop> findByName(String name);

    List<ExpenseShop> findByUser(AppUser user);
}
