package com.github.happiexplorer.example.endpoint;

import com.github.happiexplorer.example.security.Roles;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DeclareRoles({
        Roles.GENDER_MANAGER,
        Roles.CUSTOMER_MANAGER
})
@ApplicationPath("api")
public class WebConfiguration extends Application {

    public WebConfiguration() {
        // NO-OP
    }
}
