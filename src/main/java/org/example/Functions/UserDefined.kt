package org.example.Functions

import org.example.Generator.Context
import org.example.Generator.OpcodeList
import org.example.Utils.Utils
import org.example.ast.AccessibleTreeNode
import org.example.ast.AtomTreeNode
import org.example.ast.TreeNode

object UserDefined:FunctionTranslator<Int>() {
    override var functions: MutableMap<String, Int> = mutableMapOf()
    private var serviceFrameAtoms = 0

    fun setServiceFrames(serviceFrames: Int){
        serviceFrameAtoms = serviceFrames
    }
    fun add(name: String, address: Int){
        functions[name] = address
    }

    override fun call(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("PUSH")
        val backAddr = opcodeList.size() - 1
        opcodeList.add("PUSH",
            functions[(treeNode.childNodes[0] as AccessibleTreeNode<*>).getValue()]?.let { Utils.decToHex(it, 2 * addressLen) })
        opcodeList.add("JUMP")

        opcodeList.add("JUMPDEST")
        opcodeList.list[backAddr].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * addressLen)
    }


}