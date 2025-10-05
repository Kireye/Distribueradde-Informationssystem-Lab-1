package com.werkstrom.distinfolab1.bo.enums;

public enum UserRole {
    ADMIN(3, "admin"),
    STAFF(2, "staff"),
    CUSTOMER(1, "customer"),
    GUEST(0, "guest");

    private final int privilegeLevel;
    private final String roleName;

    UserRole(int privilegeLevel, String roleName) {
        this.privilegeLevel = privilegeLevel;
        this.roleName = roleName;
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return      "{privilegeLevel: " + this.privilegeLevel
                + ", roleName: " + this.roleName + "}";
    }
}