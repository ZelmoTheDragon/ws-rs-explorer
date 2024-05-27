package com.github.happiexplorer.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.util.EnumSet;
import java.util.Set;

/**
 * An identity store for validate token credential.
 */
@ApplicationScoped
public class TokenIdentityStore implements IdentityStore {

    /**
     * Security manager for this module.
     */
    @Inject
    private HappiSecurityManager happiSecurityManager;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public TokenIdentityStore() {
        // NO-OP
    }


    @Override
    public CredentialValidationResult validate(final Credential credential) {

        CredentialValidationResult result;

        if (credential instanceof TokenCredential tokenCredential) {
            var payload = tokenCredential.decodePayload();
            var jsonObject = payload.getRawData();

            var claimUsername = happiSecurityManager
                    .getConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_USERNAME);

            var claimGroups = happiSecurityManager
                    .getConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_GROUPS);

            var callerName = jsonObject.getJsonString(claimUsername).getString();
            var groups = Set.copyOf(jsonObject.getJsonArray(claimGroups).getValuesAs(JsonString::getString));

            result = new CredentialValidationResult(callerName, groups);
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
