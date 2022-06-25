package com.github.ws.rs.explorer.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import jakarta.security.enterprise.credential.Credential;

import com.github.ws.rs.explorer.Jsons;


public class JsonWebTokenSignedHashMac implements Credential {

    private static final int HEADER_INDEX = 0;

    private static final int PAYLOAD_INDEX = 1;

    private static final int SIGNATURE_INDEX = 2;

    private static final int TOKENS_LENGTH = 3;

    private String token;

    private String secret;

    public JsonWebTokenSignedHashMac(final String token, final String secret) {
        this.token = Objects.requireNonNull(token);
        this.secret = Objects.requireNonNull(secret);
    }

    @Override
    public boolean isCleared() {
        return Objects.isNull(this.token) && Objects.isNull(this.secret);
    }

    @Override
    public void clear() {
        this.token = null;
        this.secret = null;
    }

    @Override
    public boolean isValid() {
        boolean valid;
        if (this.isCleared()) {
            valid = false;
        } else {
            var tokens = token.split("\\.");
            if (tokens.length == TOKENS_LENGTH) {
                var base64Header = tokens[HEADER_INDEX];
                var base64Payload = tokens[PAYLOAD_INDEX];
                var sign = tokens[SIGNATURE_INDEX];
                var alg = getAlgorithmFromHeader(base64Header);
                var input = String.join(".", base64Header, base64Payload);
                var output = HashMac.execute(alg, this.secret, input);
                valid = Objects.equals(sign, output);
            } else {
                throw new IllegalArgumentException("Wrong token format, expected <header>.<payload>.<sign>");
            }
        }
        return valid;
    }

    private static String getAlgorithmFromHeader(final String base64Header) {
        var data = Base64.getUrlDecoder().decode(base64Header);
        var json = new String(data, StandardCharsets.UTF_8);
        var header = Jsons.parse(Header.class, json);

        if (!Objects.equals(header.getType(), TokenProvider.SUPPORTED_TYPE)) {
            throw new UnsupportedOperationException("Unsupported 'typ' : " + header.getAlgorithm());
        }

        return HashMac.SUPPORTED_ALGORITHM.get(header.getAlgorithm());
    }



}
