package org.example.CFG

class CycleGraphCount {
    private var edges = 0
    private var nodes = 0
    private var hashSet = HashSet<CFGNode>()

    private fun toInt(b: Boolean): Int {
        return if (b) {1} else {0}
    }
    private fun run(nodeStart: CFGNode) {
        if (nodeStart in hashSet){
            return
        }
        nodes += 1
        hashSet.add(nodeStart)

        arrayOf(nodeStart.toFalse, nodeStart.toTrue, nodeStart.toStraight).forEach {
            if(it != null) {
                edges += 1
                run(it)
            }
        }
    }
    fun run(nodeStart: FunctionNode): Int {
        nodeStart.cfg?.let { run(it) }
        return edges - nodes + 2
    }
}