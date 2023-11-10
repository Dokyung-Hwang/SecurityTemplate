package com.sample.sample.account.constants;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Authority {
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private final String description;


    public String getName() {
        return name();
    }

    public String getDescription() {
        return this.description;
    }
}
