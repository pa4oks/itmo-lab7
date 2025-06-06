package ru.se.ifmo.db;

/**
 * Defines a contract for encoding strings (for example, passwords) and verifying
 * encoded values against raw inputs.
 */
public interface Encoder {

    /**
     * Encodes the given raw input into a hashed form.
     *
     * @param raw the raw string to encode
     * @return the encoded (hashed) representation
     */
    String encode(String raw);

    /**
     * Verifies that the given raw input, when encoded, matches the supplied hash.
     *
     * @param raw  the raw string to verify
     * @param hash the expected encoded representation
     * @return {@code true} if {@code encode(raw).equals(hash)}, {@code false} otherwise
     */
    boolean verify(String raw, String hash);
}

