package org.example.exceptions

class SemanticAnalysisExceptionWithLine(
    msg: String,
    line: Int,
    col: Int,
) :
    SemanticAnalysisException(String.format("On line %d:%d: %s",
        line, col, msg))

open class SemanticAnalysisException(msg: String) : Exception(msg)