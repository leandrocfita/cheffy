package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Menu {

    private final Set<FoodItem> items = new HashSet<>();
    private boolean active = true;

    void addItem(FoodItem item) {
        if (!active) {
            throw new IllegalStateException("Cannot add items to inactive menu");
        }
        items.add(item);
    }

    void removeItem(UUID id) {
        boolean removed = items.removeIf(item -> item.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Food item not found in menu");
        }
    }

    Set<FoodItem> availableItems() {
        if (!active) {
            return Set.of();
        }

        return items.stream()
                .filter(FoodItem::isAvailable)
                .collect(Collectors.toUnmodifiableSet());
    }

    void deactivate() {
        this.active = false;
        items.forEach(FoodItem::disable);
    }

    boolean hasActiveItems() {

        return items != null
                && !items.isEmpty()
                && items.stream().anyMatch(FoodItem::isActive);
    }
}
