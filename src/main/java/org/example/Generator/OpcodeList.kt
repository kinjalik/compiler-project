package org.example.Generator

import jdk.internal.icu.impl.Utility.hex

object CounterOpcode{
    private var counter = 0
    fun count(): Int = counter++
    fun plus(x: Int) {
        counter += x
    }

    fun reset() {
        counter = 0
    }
}

fun decToHex(number: Int, pad: Int): String {
    return String.format("0x%${pad}X", number).substring(2)
}
class Opcode(
    name: String,
    addressLength: Int,
    extraValue: String? = null,
    instructionSet: Map<String, String>? = null
) {
    private var id = 0
    private var __name = name
    private var __extraValue: String? = null
    private var __instructionSet: Map<String, String>? = instructionSet

    init {
        id = CounterOpcode.count()
        if (extraValue != null) {
            __extraValue = extraValue
        } else if (name == "PUSH") {
            __extraValue = decToHex(0, 2 * addressLength)
        } else {
            __extraValue = null
        }
        if (name == "PUSH") {
            CounterOpcode.plus(addressLength)
        }
    }

    fun get_str(): String{
        val a: String? = __instructionSet?.get(__name)
        var b: String? = null
        if (__name == "PUSH") {
            b = __extraValue
        }

        return a + b
    }
}
class OpcodeList
    (private var addressLength: Int) {

    private var opcodeList: Map<String, String> = mapOf()
    private var extraValue: String? = null
    private var list = ArrayList<Opcode>()

    fun add(name: String, extraValue: String? = null) {
        list.add(Opcode(name, addressLength, extraValue, opcodeList))
    }

    fun get_str(): String {
        var res = ""
        for (oc in list) {
            res += oc.get_str()
        }
        return res
    }

    init {
        opcodeList = mapOf(
            "STOP" to "00",
            "ADD" to "01",
            "MUL" to "02",
            "SUB" to "03",
            "DIV" to "04",
            "MOD" to "06",
            "ADDMOD" to "08",
            "MULMOD" to "09",
            "EXP" to "0a",
            "LT" to "10",
            "GT" to "11",
            "SLT" to "12",
            "SGT" to "13",
            "EQ" to "14",
            "ISZERO" to "15",
            "AND" to "16",
            "OR" to "17",
            "XOR" to "18",
            "NOT" to "19",
            "CALLDATALOAD" to "35",
            "MLOAD" to "51",
            "MSTORE" to "52",
            "JUMP" to "56",
            "JUMPI" to "57",
            "JUMPDEST" to "5b",
            "PUSH" to  "%x".format(0x60 + addressLength - 1), // TODO without 0x
            "DUP1" to "80",
            "DUP2" to "81",
            "SWAP1" to "90",
            "RETURN" to "f3",
        )
    }
}
