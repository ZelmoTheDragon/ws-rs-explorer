package com.github.ws.rs.explorer.security;

import java.util.EnumSet;
import java.util.Set;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

@Singleton
public class TokenIdentityStore implements IdentityStore {

    private final SecurityManager securityManager;

    @Inject
    public TokenIdentityStore(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public CredentialValidationResult validate(final Credential credential) {

        CredentialValidationResult result;

        if (credential instanceof TokenCredential tokenCredential) {
            var payload = tokenCredential.decodePayload();
            var jsonObject = payload.getRawData();

            var claimUsername = securityManager
                    .getConfiguration(SecurityManager.Configuration.TOKEN_CLAIM_USERNAME);

            var claimGroups = securityManager
                    .getConfiguration(SecurityManager.Configuration.TOKEN_CLAIM_GROUPS);

            var callerName = jsonObject.getJsonString(claimUsername).getString();
            var groups = jsonObject.getJsonArray(claimGroups).getValuesAs(String::valueOf);

            result = new CredentialValidationResult(callerName, Set.copyOf(groups));
        } else {
            result = CredentialValidationResult.INVALID_RESULT;
        }
        return result;
    }

    @Override
    public Set<String> getCallerGroups(final CredentialValidationResult validationResult) {
        return Set.copyOf(validationResult.getCallerGroups());
    }


    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS);
    }

}
