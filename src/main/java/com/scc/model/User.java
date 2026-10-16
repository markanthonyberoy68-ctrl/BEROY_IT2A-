package model;

public record User(
        int id,
        String username,
        String roles,
        String status
) {
}