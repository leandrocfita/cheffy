package br.com.fiap.cheffy.domain.user.entity;

import java.util.Objects;

public class Address {

    private final Long id;
    private final String streetName;
    private final Integer number;
    private final String city;
    private final String postalCode;
    private final String neighborhood;
    private final String stateProvince;
    private final String addressLine;

    private boolean main;
    private User user;

    public Address(
            Long id,
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            boolean main) {

        this.id = id;
        this.streetName = Objects.requireNonNull(streetName);
        this.number = Objects.requireNonNull(number);
        this.city = Objects.requireNonNull(city);
        this.postalCode = Objects.requireNonNull(postalCode);
        this.neighborhood = Objects.requireNonNull(neighborhood);
        this.stateProvince = Objects.requireNonNull(stateProvince);
        this.addressLine = addressLine;
        this.main = main;
    }

    public static Address create(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            boolean isMain
    ) {
        return new Address(
                null,
                streetName,
                number,
                city,
                postalCode,
                neighborhood,
                stateProvince,
                addressLine,
                isMain
        );
    }

    /* Relationship control */

    void attachTo(User user) {
        this.user = user;
    }

    void detach() {
        this.user = null;
    }

    /* State changes */

    void setMain(boolean main) {
        this.main = main;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public boolean isMain() {
        return main;
    }

    public User getUser() {
        return user;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getAddressLine() {
        return addressLine;
    }
}