package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import jakarta.persistence.Column;

import java.time.OffsetTime;
import java.util.Objects;
import java.util.UUID;

public class Restaurant {

    private final UUID id;
    private String name;
    private String cnpj;
    private OffsetTime openingTime;
    private OffsetTime closingTime;
    private String culinary;
    private Address address;
    private User user;

    private Menu menu;
    private boolean active;

    public Restaurant(
            UUID id,
            String name,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime,
            Address address,
            User user) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.culinary = Objects.requireNonNull(culinary);
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.address = address;
        this.user = user;
        this.menu = new Menu();
        this.active = true;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public void addFoodItem(FoodItem item) {

        menu.addItem(item);
    }

    public void removeFoodItem(UUID itemId) {

        menu.removeItem(itemId);
    }

    public Menu getMenu() {
        return menu;
    }
}

