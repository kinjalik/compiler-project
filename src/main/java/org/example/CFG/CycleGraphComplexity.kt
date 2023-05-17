package org.example.CFG

import org.example.WarningCollection.WarningCollection

object CycleGraphComplexity {
    private val complexity = 20
    fun run(cfg: CFG) {
        cfg.functions.forEach {
            if (CycleGraphCount().run(it) > complexity) {
                WarningCollection.addWarning("Function \"${it.functionName}\" is complex. Simplify it")
            }
        }
    }
}