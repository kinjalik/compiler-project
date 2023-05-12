package org.example.exceptions

class ByteBaseOverflowException (
    base: Int,
    value: Int
):
Exception(String.format("Expected value with 32 byte max size, got value %d with base %d", value, base))