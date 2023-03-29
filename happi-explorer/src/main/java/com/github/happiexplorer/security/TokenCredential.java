package com.github.happiexplorer.security;

import com.github.happiexplorer.ExplorerException;
import com.github.happiexplorer.Jsons;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.credential.Credential;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Credential for token authentication.
 * The token format is: {@code <header>.<payload>.<sign>}
 */
public final class TokenCredential implements Credential {

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
        return !this.isCleared()
                && this.isValidSignature()
                && this.isValidTime();
    }


    /**
     * Decode as object the <i>JWT</i> header.
     *
     * @return The header as object
     */
    public Header decodeHeader() {
        return decode(Header.class, Header::setRawData, this.tokens[HEADER_INDEX]);
    }

    /**
     * Decode as object the <i>JWT</i> payload.
     *
     * @return The payload as object
     */
    public Payload decodePayload() {
        return decode(Payload.class, Payload::setRawData, this.tokens[PAYLOAD_INDEX]);
    }

    /**
     * Get the signature from token.
     *
     * @return The signature
     */
    public String getSignature() {
        return this.tokens[SIGNATURE_INDEX];
    }

    /**
     * Verify if the token signature is valid.
     *
     * @return The value {@code true} if the token signature is valid,
     * otherwise the value {@code false} is returned
     */
    private boolean isValidSignature() {
        var base64Header = this.tokens[HEADER_INDEX];
        var base64Payload = this.tokens[PAYLOAD_INDEX];
        var sign = this.getSignature();
        var alg = extractAlgorithmFromHeader();
        var input = String.join(".", base64Header, base64Payload);
        var output = HashMac.execute(alg, this.secret, input);
        return Objects.equals(sign, output);
    }

    /**
     * Verify if the token is in right time range.
     *
     * @return The value {@code true} if the token is in right time range,
     * otherwise the value {@code false} is returned
     */
    private boolean isValidTime() {
        var payload = decodePayload();
        var now = System.currentTimeMillis() / 1000L;
        var exp = Optional.ofNullable(payload.getExpirationTime()).orElse(0L);
        var nbf = Optional.ofNullable(payload.getNotBeforeTime()).orElse(0L);

        return exp > now && nbf < now;
    }

    /**
     * Extract the algorithm from header.
     *
     * @return The algorithm used for the signature
     * @throws ExplorerException If the header contains an unsupported token type of algorithm
     */
    private String extractAlgorithmFromHeader() {
        var header = decodeHeader();
        if (!Objects.equals(header.getType(), TokenCredentialFactory.SUPPORTED_TYPE)) {
            throw new ExplorerException("Unsupported 'typ' : " + header.getAlgorithm());
        }
        var alg = TokenCredentialFactory.SUPPORTED_ALGORITHM.get(header.getAlgorithm());

        return Optional
                .ofNullable(alg)
                .orElseThrow(() -> new ExplorerException("Unsupported 'alg' :" + alg));
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
