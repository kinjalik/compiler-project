package org.example.tokens;

import org.jetbrains.annotations.NotNull;

/**
 * This token is not used in actual compilation process and kept only for debugging purposes
 */
public class PhantomToken extends Token {
    private char value;

    PhantomToken(char value) {
        this.value = value;
    }

    @NotNull
    @Override
    public String toString() {
        return Character.toString(value);
    }

    @NotNull
    @Override
    public String reverse() {
        return toString();
    }
}
