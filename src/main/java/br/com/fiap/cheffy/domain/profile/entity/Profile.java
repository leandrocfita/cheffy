package br.com.fiap.cheffy.domain.profile.entity;

import java.util.Objects;

public class Profile {

    private final Long id;
    private final String type;

    private Profile(Long id, String type) {
        this.id = id;
        this.type = Objects.requireNonNull(type);
    }

    public static Profile create(Long id, String profileType){
        return new Profile(id, profileType);
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
