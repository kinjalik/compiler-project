package org.example.exceptions
class CFGException(
    msg: String,

) :
    Exception(String.format(msg))
