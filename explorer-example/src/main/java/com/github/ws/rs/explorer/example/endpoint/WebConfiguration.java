package com.github.ws.rs.explorer.example.endpoint;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import com.github.ws.rs.explorer.example.security.Roles;

@DeclareRoles({
        Roles.GENDER_MANAGER,
        Roles.CUSTOMER_MANAGER
})
@ApplicationPath("api")
public class WebConfiguration extends Application {

    public WebConfiguration() {
    }
}
