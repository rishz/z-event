package model;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID user_uuid;
    private String username;
    private String password;
    private String name;

}
