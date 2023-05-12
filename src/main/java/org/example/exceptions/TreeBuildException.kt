package org.example.exceptions

class TreeBuildException(
    line: Int,
    col: Int,
    elem: String
) :
    Exception(String.format("Unknown token on line %d:%d: while parsing element: %s",
        line, col, elem))