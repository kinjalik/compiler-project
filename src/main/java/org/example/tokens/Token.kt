package org.example.tokens

abstract class Token {

    /**
     * Returns describing representation of the token
     */
    abstract override fun toString(): String

    /**
     * Returns the code represented by this token
     */
    abstract fun reverse(): String
}