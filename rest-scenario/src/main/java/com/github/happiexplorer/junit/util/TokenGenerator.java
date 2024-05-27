package com.github.happiexplorer.junit.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;


/**
 * Utility class for token generation.
 */
public final class TokenGenerator {

    /**
     * Default username.
     */
    private static final String USERNAME = "john.doe";

    /**
     * Default groups.
     */
    private static final String GROUPS = String.join(
            ", ",
            wrap("customer-manager"),
            wrap("gender-manager")
    );

    /**
     * Default token secret key.
     */
    private static final String SECRET = "secret";

    /**
     * Template for token header.
     */
    private static final String HEADER_TEMPLATE =
            """
                    {
                        "typ": "JWT",
                        "alg": "HS256"
                    }
                    """;

    /**
     * Template for token payload.
     */
    private static final String PAYLOAD_TEMPLATE =
            """
                    {
                        "iss": "happi-explorer-example",
                        "sub": "{{sub}}",
                        "aud": "example",
                        "exp": "{{exp}}",
                        "nbf": "{{nbf}}",
                        "iat": "{{iat}}",
                        "preferred_username": "{{preferred_username}}",
                        "groups": [{{groups}}]
                    }
                    """;

    /**
     * Internal constructor.
     * Instantiation is not allowed.
     */
    private TokenGenerator() {
        throw new UnsupportedOperationException("Instantiation is not allowed");

    }

    /**
     * Generate a new token.
     *
     * @return A token
     */
    public static String generateNewToken() {

        var base64Header = Base64.getUrlEncoder().encodeToString(HEADER_TEMPLATE.getBytes(StandardCharsets.UTF_8));

        // now with a few seconds in the past, because tests run too fast...
        var now = System.currentTimeMillis() / 1000L - 60L;
        var exp = now + 1800L;

        var payload = PAYLOAD_TEMPLATE
                .replace("{{sub}}", UUID.randomUUID().toString())
                .replace("{{exp}}", String.valueOf(exp))
                .replace("{{nbf}}", String.valueOf(now))
                .replace("{{iat}}", String.valueOf(now))
                .replace("{{preferred_username}}", USERNAME)
                .replace("{{groups}}", GROUPS);

        var base64Payload = Base64.getUrlEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        var input = String.join(".", base64Header, base64Payload);
        var signature = HashMac.execute("HmacSHA256", SECRET, input);

        return String.join(".", base64Header, base64Payload, signature);
    }

    /**
     * Wrap a text with double quotes.
     *
     * @param text Any text
     * @return The text wrapped
     */
    private static String wrap(final String text) {
        return "\"" + text + "\"";
    }
}
