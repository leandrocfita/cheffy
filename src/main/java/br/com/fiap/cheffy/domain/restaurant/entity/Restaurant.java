package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import jakarta.persistence.Column;

import java.time.Duration;
import java.time.OffsetTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_INVALID_WORKING_TIME;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.WORKING_TIME_TOO_SHORT;

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

    protected Restaurant(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.cnpj = Objects.requireNonNull(cnpj);
        this.culinary = Objects.requireNonNull(culinary);
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.menu = new Menu(new HashSet<>());
        this.active = true;
    }

    Restaurant(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime,
            boolean active,
            Address address,
            User user,
            Menu menu
    ) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.culinary = culinary;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.active = active;
        this.address = address;
        this.user = user;
        this.menu = menu;
    }

    public static Restaurant createRestaurant(
            String name,
            String cnpj,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime,
            User user
    ){
        Restaurant restaurant = new Restaurant(
                null,
                name,
                cnpj,
                culinary,
                openingTime,
                closingTime
        );
        restaurant.validateWorkingTime();
        restaurant.setOwner(user);
        return restaurant;
    }

    public static Restaurant reconstitute(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime,
            boolean active,
            Address address,
            User user,
            Menu menu
    ) {
        Restaurant restaurant = new Restaurant(
                id,
                name,
                cnpj,
                culinary,
                openingTime,
                closingTime,
                active,
                address,
                user,
                menu);

        return restaurant;
    }

    private void validateWorkingTime() {
        if (openingTime.equals(closingTime)) {
            throw new UserOperationNotAllowedException(
                    RESTAURANT_INVALID_WORKING_TIME
            );
        }

        Duration duration = Duration.between(openingTime, closingTime);
        if (duration.toHours() < 1) {
            throw new UserOperationNotAllowedException(WORKING_TIME_TOO_SHORT);
        }
    }

    public void setOwner(User user) {
        this.user = user;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public void addFoodItem(FoodItem item) {

        item.setRestaurant(this);
        menu.addItem(item);
    }

    public void removeFoodItem(UUID itemId) {

        FoodItem removed = menu.removeItem(itemId);
        removed.setRestaurant(null);
    }

    public Menu getMenu() {

        return menu;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public OffsetTime getOpeningTime() {
        return openingTime;
    }

    public OffsetTime getClosingTime() {
        return closingTime;
    }

    public String getCulinary() {
        return culinary;
    }

    public Address getAddress() {
        return address;
    }

    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }
}

