package org.example.builtins
import org.example.Generator.CodeGenerator
import org.example.Generator.Context
import org.example.Generator.OpcodeList
import org.example.ast.TreeNode
import org.example.Utils.Utils
import org.example.ast.AtomTreeNode

object BuiltIns {

    var addressLen = 1;
    private val functions: Map<String,
                (TreeNode, Context, OpcodeList) -> Unit> = mapOf(
        "plus" to ::__plus,
        "minus" to ::__minus,
        "times" to ::__times,
        "divide" to ::__divide,
        "equal" to ::__equal,
        "nonequal" to ::__nonequal,
        "less" to ::__less,
        "lesseq" to ::__lesseq,
        "greater" to ::__greater,
        "greatereq" to ::__greatereq,
        "and" to ::__and,
        "or" to ::__or,
        "not" to ::__not,
        "read" to ::__read,

        "setq" to ::__setq,
        "retrun" to ::__return,
        "read" to ::__read
    )

    fun has(name: String): Boolean {
        return functions.contains(name)
    }

    fun call(treeNode: TreeNode, context: Context, opcodeList: OpcodeList){
        functions[(treeNode.childNodes[0] as AtomTreeNode).getValue()]?.let { it(treeNode, context, opcodeList) }
    }

    fun setAddrLen(addrLen: Int){
        addressLen = addrLen
    }

    private fun __plus(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
       opcodeList.add("ADD")
    }

    private fun __minus(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("SWAP1")
        opcodeList.add("SUB")
    }
    private fun __times(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("MUL")
    }
    private fun __divide(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("SWAP1")
        opcodeList.add("DIV")
    }
    private fun __equal(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("EQ")
    }

    private fun __nonequal(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("EQ")
        opcodeList.add("PUSH", Utils.decToHex(0, 2 * addressLen))
        opcodeList.add("EQ")
    }

    private fun __less(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("GT")
    }

    private fun __lesseq(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("LT")
        opcodeList.add("ISZERO")
    }

    private fun __greater(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("LT")
    }

    private fun __greatereq(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("GT")
        opcodeList.add("ISZERO")
    }

    private fun __and(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
       opcodeList.add("AND")
    }

    private fun __or(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("OR")
    }

    private fun __not(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
       opcodeList.add("PUSH", Utils.decToHex(0, 2* addressLen))
       opcodeList.add("EQ")
    }

    private fun __read(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("PUSH", Utils.decToHex(0x20, 2 * addressLen))
        opcodeList.add("MUL")

        opcodeList.add("CALLDATALOAD")
    }

    private fun __setq(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        //TODO need memory
        val atomName = (treeNode.childNodes[1] as AtomTreeNode).getValue()
        val address = context.getAtomAddress(atomName)
        VirtualStackHelper.storeAtomValue(opcodeList, address)
    }

    private fun __return(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        //TODO need memory
        if(context.is_prog){
            opcodeList.add("PUSH", Utils.decToHex(0, 2 * addressLen))
            opcodeList.add("MSTORE")
            opcodeList.add("PUSH", Utils.decToHex(32, 2 * addressLen))
            opcodeList.add("PUSH", Utils.decToHex(0, 2 * addressLen))
            opcodeList.add("RETURN")
            return
        }
        VirtualStackHelper.loadBackAddress(opcodeList)
        VirtualStackHelper.removeFrame(opcodeList)
        opcodeList.add("JUMP")
    }
}

