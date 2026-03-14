package br.com.fiap.cheffy.application.profile.dto;

public class SortRequestPort {

    private final String field;
    private final Direction direction;

    public SortRequestPort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction {
        ASC,
        DESC
    }
}
