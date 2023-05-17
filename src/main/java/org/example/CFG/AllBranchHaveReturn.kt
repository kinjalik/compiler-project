package org.example.CFG

import org.example.exceptions.CFGException

class AllBranchHaveReturn(
    val node: FunctionNode? = null,
    val cfg: CFG? = null) {
    private var hashSet = HashSet<CFGNode>()

    private fun toInt(b: CFGNode?): Int {
        return if (b != null) {1} else {0}
    }
    private fun run(nodeStart: CFGNode) {
        if (nodeStart in hashSet){
            return
        }
        hashSet.add(nodeStart)

        var flag = false
        arrayOf(nodeStart.toFalse, nodeStart.toTrue, nodeStart.toStraight).forEach {
            if(it != null) {
                flag = true
                run(it)
            }
        }
        if (!flag && !nodeStart.isReturn || toInt(nodeStart.toFalse) + toInt(nodeStart.toTrue) == 1) {
            throw CFGException("\"return\" is needed in function \"${node?.functionName}\"")
        }
    }
    fun run() {
        node?.cfg?.let { run(it) }
        cfg?.functions?.forEach {
            it.cfg?.let { it1 -> run(it1) }
        }
    }
}