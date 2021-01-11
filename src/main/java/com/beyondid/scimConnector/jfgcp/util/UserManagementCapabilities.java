package com.beyondid.scimConnector.jfgcp.util;

public enum UserManagementCapabilities {
    GROUP_PUSH,
    IMPORT_NEW_USERS,
    IMPORT_PROFILE_UPDATES,
    PUSH_NEW_USERS,
    PUSH_PASSWORD_UPDATES,
    PUSH_PENDING_USERS,
    PUSH_PROFILE_UPDATES,
    PUSH_USER_DEACTIVATION,
    REACTIVATE_USERS;

    private UserManagementCapabilities() {
    }
}
