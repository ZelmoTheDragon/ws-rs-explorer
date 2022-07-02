package com.github.ws.rs.explorer.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.credential.Credential;

import com.github.ws.rs.explorer.Jsons;

/**
 * Credential for token authentication.
 * The token format is: {@code <header>.<payload>.<sign>}
 */
final class TokenCredential implements Credential {

    /**
     * Header index in encoded token.
     */
    private static final int HEADER_INDEX = 0;

    /**
     * Payload index in encoded token.
     */
    private static final int PAYLOAD_INDEX = 1;

    /**
     * Signature index in encoded token.
     */
    private static final int SIGNATURE_INDEX = 2;

    /**
     * Number of element in token.
     */
    private static final int TOKENS_LENGTH = 3;

    /**
     * Token split.
     */
    private String[] tokens;

    /**
     * Secret for compute the token signature.
     */
    private String secret;

    /**
     * Construct a token credential.
     *
     * @param token  Full encoded token
     * @param secret Secret for compute the token signature
     */
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
            var alg = extractAlgorithmFromHeader();
            var input = String.join(".", base64Header, base64Payload);
            var output = HashMac.execute(alg, this.secret, input);
            valid = Objects.equals(sign, output);
        }
        return valid;
    }


    /**
     * Decode as object the <i>JWT</i> header.
     *
     * @return The header as object
     */
    Header decodeHeader() {
        return decode(Header.class, Header::setRawData, this.tokens[HEADER_INDEX]);
    }

    /**
     * Decode as object the <i>JWT</i> payload.
     *
     * @return The payload as object
     */
    Payload decodePayload() {
        return decode(Payload.class, Payload::setRawData, this.tokens[PAYLOAD_INDEX]);
    }

    /**
     * Extract the signature from token.
     *
     * @return The signature
     */
    String getSignature() {
        return this.tokens[SIGNATURE_INDEX];
    }

    /**
     * Extract the algorithm from header.
     *
     * @return The algorithm used for the signature
     * @throws UnsupportedOperationException If the header contains an unsupported token type of algorithm
     */
    private String extractAlgorithmFromHeader() {
        var header = decodeHeader();
        if (!Objects.equals(header.getType(), TokenCredentialFactory.SUPPORTED_TYPE)) {
            throw new UnsupportedOperationException("Unsupported 'typ' : " + header.getAlgorithm());
        }
        var alg = HashMac.SUPPORTED_ALGORITHM.get(header.getAlgorithm());

        return Optional
                .ofNullable(alg)
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported 'alg' :" + alg));
    }

    /**
     * Decode <i>JWT</i> element as object.
     *
     * @param type   Object class representing the <i>JSON</i> element
     * @param setter Setter for store the raw <i>JSON</i> object data
     * @param base64 Base64 encoded data
     * @param <T>    Generic type
     * @return An instance of object representing the <i>JSON</i> element
     */
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
