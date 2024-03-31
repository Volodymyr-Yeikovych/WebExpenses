package pl.edu.s28201.webExpenses.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.webExpenses.model.AppUser;
import pl.edu.s28201.webExpenses.model.expense.Expense;
import pl.edu.s28201.webExpenses.model.expense.ExpenseShop;
import pl.edu.s28201.webExpenses.repository.ExpenseRepository;
import pl.edu.s28201.webExpenses.repository.ExpenseShopRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShopService {
    private final UuidService uuidService;
    private final ExpenseShopRepository shopRepository;
    private final SecurityService securityService;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public ShopService(UuidService uuidService, ExpenseShopRepository shopRepository, SecurityService securityService, ExpenseRepository expenseRepository) {
        this.uuidService = uuidService;
        this.shopRepository = shopRepository;
        this.securityService = securityService;
        this.expenseRepository = expenseRepository;
    }

    public void hideAllById(String shops) {
        if (shops != null && !shops.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(shops, ",");
            StringBuilder deleteMsg = new StringBuilder("Parsed Shop IDs to Hide: [");
            for (UUID id : ids) {
                if (isValidShopId(id, securityService.getUserFromSecurity())) {
                    shopRepository.hideById(id);
                    deleteMsg.append(id).append(",");
                }
            }
            deleteMsg.append("]");
            log.info(deleteMsg.toString());
        } else {
            log.info("No shops to hide");
        }
    }

    private boolean isValidShopId(UUID id, AppUser currentUser) {
        Optional<ExpenseShop> shopOpt = shopRepository.findById(id);

        if (shopOpt.isEmpty()) return false;

        ExpenseShop shop = shopOpt.get();
        return shop.getUser().equals(currentUser);
    }

    public List<ExpenseShop> findAll() {
        return shopRepository.findAllByUserAndNotHidden(securityService.getUserFromSecurity());
    }

    public List<ExpenseShop> findAllExisting() {
        AppUser currentUser = securityService.getUserFromSecurity();

        List<ExpenseShop> existingShops = expenseRepository
                .findExpensesByUser(currentUser)
                .stream()
                .map(Expense::getShop)
                .distinct()
                .collect(Collectors.toList());

        List<ExpenseShop> allShops = shopRepository.findByUser(currentUser);
        for (ExpenseShop shop : allShops) {
            if (!existingShops.contains(shop)) {
                if (shop.isHidden()) {
                    shopRepository.delete(shop);
                } else {
                    existingShops.add(shop);
                }
            }
        }

        return existingShops;
    }

    public List<ExpenseShop> parseIdsToShops(String shopStr) {
        List<ExpenseShop> shops = new ArrayList<>();
        if (shopStr != null && !shopStr.isEmpty()) {
            List<UUID> ids = uuidService.parseExpenseIds(shopStr, ",");
            for (UUID id : ids) {
                if (isValidShopId(id, securityService.getUserFromSecurity())) {
                    shops.add(shopRepository.findById(id).get());
                }
            }
        }
        return shops;
    }
}
