package com.github.ws.rs.explorer.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.function.BiConsumer;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.credential.Credential;

import com.github.ws.rs.explorer.Jsons;


class TokenCredential implements Credential {

    private static final int HEADER_INDEX = 0;

    private static final int PAYLOAD_INDEX = 1;

    private static final int SIGNATURE_INDEX = 2;

    private static final int TOKENS_LENGTH = 3;

    private String[] tokens;

    private String secret;

    public TokenCredential(final String token, final String secret) {
        this.secret = secret;
        this.tokens = token.split("\\.");
        if (tokens.length != TOKENS_LENGTH) {
            throw new IllegalArgumentException("Wrong token format, expected <header>.<payload>.<sign>");
        }
    }

    @Override
    public boolean isCleared() {
        return Objects.isNull(this.tokens) && Objects.isNull(this.secret);
    }

    @Override
    public void clear() {
        this.tokens = null;
        this.secret = null;
    }

    @Override
    public boolean isValid() {
        boolean valid;
        if (this.isCleared()) {
            valid = false;
        } else {
            var base64Header = this.tokens[HEADER_INDEX];
            var base64Payload = this.tokens[PAYLOAD_INDEX];
            var sign = this.tokens[SIGNATURE_INDEX];
            var alg = extractAlgorithmFromHeader(base64Header);
            var input = String.join(".", base64Header, base64Payload);
            var output = HashMac.execute(alg, this.secret, input);
            valid = Objects.equals(sign, output);
        }
        return valid;
    }


    Header decodeHeader() {
        return decode(Header.class, Header::setRawData, this.tokens[HEADER_INDEX]);
    }

    Payload decodePayload() {
        return decode(Payload.class, Payload::setRawData, this.tokens[PAYLOAD_INDEX]);
    }

    String getSignature() {
        return this.tokens[SIGNATURE_INDEX];
    }

    private String extractAlgorithmFromHeader(final String base64Header) {
        var header = decodeHeader();
        if (!Objects.equals(header.getType(), TokenProvider.SUPPORTED_TYPE)) {
            throw new UnsupportedOperationException("Unsupported 'typ' : " + header.getAlgorithm());
        }
        return HashMac.SUPPORTED_ALGORITHM.get(header.getAlgorithm());
    }

    private static <T> T decode(
            final Class<T> type,
            final BiConsumer<T, JsonObject> setter,
            final String base64) {

        var data = Base64.getUrlDecoder().decode(base64);
        var json = new String(data, StandardCharsets.UTF_8);
        var jsonObject = Jsons.parse(json);
        var entity = Jsons.parse(type, json);
        setter.accept(entity, jsonObject);
        return entity;
    }

}
