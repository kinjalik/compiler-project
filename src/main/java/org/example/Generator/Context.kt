package org.example.Generator

class Context(var idCounter: Int) {
    var nameByNum: MutableMap<Int, String> = mutableMapOf()
    private val numByName: MutableMap<String, Int> = mutableMapOf()
    var isProg: Boolean = false

    fun getAtomAddr(name: String): Pair<Int, Boolean> {
        var isAdded = false
        if (!numByName.contains(name)) {
            isAdded = true
            numByName[name] = idCounter
            nameByNum[idCounter] = name
            idCounter += 1
        }
        return Pair(numByName[name]?.times(32) ?: -1, isAdded)
    }
}