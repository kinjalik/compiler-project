package org.example.Functions

import org.example.Generator.Context
import org.example.Generator.OpcodeList
import org.example.ast.AtomTreeNode
import org.example.ast.TreeNode


abstract class FunctionTranslator<T> {
    var addressLen = 1
    abstract var functions: Map<String, T>

    fun has(name: String): Boolean{
        return functions.contains(name)
    }

    fun setAddrLen(addrLen: Int){
        addressLen = addrLen
    }

   abstract fun call(treeNode: TreeNode, context: Context, opcodeList: OpcodeList)
}