package br.com.fiap.cheffy.domain.profile.entity;

public class Profile {

    private final Long id;
    private final String type;

    private Profile(Long id, String type) {
        validateProfile(type);
        this.id = id;
        this.type = type;
    }

    public static Profile create(Long id, String profileType){
        return new Profile(id, profileType);
    }

    public Long getId() {
        return id;
    }

    public String getType() {return type;}

    private void validateProfile(String profileType) throws IllegalArgumentException {

        if (profileType == null || profileType.isBlank()) {
            throw new IllegalArgumentException("Profile type cannot be null or empty");
        }
    }
}
