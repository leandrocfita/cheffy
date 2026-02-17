package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.UUID;

public class Restaurant {

    private final UUID id;
    private String name;
    private String culinary;
    private Menu menu;
    private boolean active;

    public Restaurant(UUID id, String name, String culinary) {
        this.id = id;
        this.name = name;
        this.culinary = culinary;
        this.menu = new Menu();
        this.active = true;
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

