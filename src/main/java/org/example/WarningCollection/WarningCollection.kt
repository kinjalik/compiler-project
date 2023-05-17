package org.example.WarningCollection

object WarningCollection {

    private var warnings = ArrayList<String>()
    fun create() {
        warnings.clear()
    }

    fun addWarning(string: String) {
        warnings.add(string)
    }

    fun printWarnings() {
        if (warnings.isEmpty()) return
        println("Warnings: ")
        warnings.forEach {
            println("\t$it")
        }
    }
}