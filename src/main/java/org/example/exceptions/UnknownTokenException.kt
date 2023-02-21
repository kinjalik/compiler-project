package org.example.exceptions

class UnknownTokenException(
    line: Int,
    col: Int,
    character: Char
) :
    Exception(String.format("Unknown token on line %d:%d: %c", line, col, character))