package br.com.fiap.cheffy.domain.fooditem.entity;

import br.com.fiap.cheffy.domain.valueobject.Money;

import java.util.Objects;
import java.util.UUID;

public class FoodItem {

    private final UUID id;

    private String name;
    private Money price;
    private boolean available;
    private boolean active;

    public FoodItem(UUID id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = true;
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    public boolean isAvailable() {
        return available && active;
    }

    public void makeUnavailable() {
        this.available = false;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Objects.equals(id, foodItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

