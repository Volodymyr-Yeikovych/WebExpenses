package pl.edu.s28201.webExpenses.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface ExpenseShopRepository extends CrudRepository<ExpenseShop, UUID> {

    Optional<ExpenseShop> findByName(String name);

    Optional<ExpenseShop> findByNameIgnoreCaseAndUser(String name, AppUser user);

    List<ExpenseShop> findByUser(AppUser user);

    default List<ExpenseShop> findAllByUserAndNotHidden(AppUser user) {
        return findByUser(user).stream().filter(shop -> !shop.isHidden()).collect(Collectors.toList());
    }

    @Modifying
    @Transactional
    default void hideAllById(List<UUID> ids) {
        for (UUID id : ids) hideById(id);
    }

    @Modifying
    @Query("UPDATE ExpenseShop s SET s.isHidden = true WHERE s.id = ?1")
    @Transactional
    void hideById(UUID id);

    @Modifying
    @Query("UPDATE ExpenseShop s SET s.isHidden = false WHERE s.id = ?1")
    @Transactional
    void showById(UUID id);
}
