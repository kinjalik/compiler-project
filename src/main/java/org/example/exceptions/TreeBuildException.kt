package org.example.exceptions

class TreeBuildException(
    private val msg: String
) : Exception(msg)