package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.restaurant.valueobject.WorkingHours;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import java.time.OffsetTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;


public class Restaurant {

    private final UUID id;
    private String name;
    private String cnpj;
    private WorkingHours workingHours;
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
            WorkingHours workingHours) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.cnpj = Objects.requireNonNull(cnpj);
        this.culinary = Objects.requireNonNull(culinary);
        this.workingHours = Objects.requireNonNull(workingHours);
        this.menu = new Menu(new HashSet<>());
        this.active = true;
    }

    public static Restaurant create24h(
            String name,
            String cnpj,
            String culinary,
            User user
    ) {
        return Restaurant.createRestaurant(
                name,
                cnpj,
                culinary,
                WorkingHours.open24Hours(),
                user
        );
    }

    public static Restaurant createWithWorkingHours(
            String name,
            String cnpj,
            String culinary,
            OffsetTime opening,
            OffsetTime closing,
            User user
    ) {
        return Restaurant.createRestaurant(
                name,
                cnpj,
                culinary,
                WorkingHours.of(opening, closing),
                user
        );
    }

    private static Restaurant createRestaurant(
            String name,
            String cnpj,
            String culinary,
            WorkingHours workingHours,
            User user
    ){
        Restaurant restaurant = new Restaurant(
                null,
                name,
                cnpj,
                culinary,
                workingHours
        );
        restaurant.setOwner(user);
        return restaurant;
    }

    //reconstitute
    protected Restaurant(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            WorkingHours workingHours,
            boolean active,
            Address address,
            User user,
            Menu menu
    ) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.culinary = culinary;
        this.workingHours = workingHours;
        this.active = active;
        this.address = address;
        this.user = user;
        this.menu = menu;
    }

    public static Restaurant reconstitute(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            OffsetTime openingTime,
            OffsetTime closingTime,
            boolean open24hours,
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
                WorkingHours.reconstitute(openingTime, closingTime, open24hours),
                active,
                address,
                user,
                menu);

        return restaurant;
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
        return workingHours.getOpeningTime();
    }

    public OffsetTime getClosingTime() {
        return workingHours.getClosingTime();
    }

    public boolean isOpen24hours() {
        return workingHours.isOpen24Hours();
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

