package org.example.Functions
import org.example.Generator.Context
import org.example.Generator.OpcodeList
import org.example.Memory.VirtualStackHelper
import org.example.ast.TreeNode
import org.example.Utils.Utils
import org.example.ast.AtomTreeNode

object BuiltIns: FunctionTranslator<(TreeNode, Context, OpcodeList) -> Unit>() {

    override var functions: Map<String,
                (TreeNode, Context, OpcodeList) -> Unit> = mapOf(
        "plus" to BuiltIns::__plus,
        "minus" to BuiltIns::__minus,
        "times" to BuiltIns::__times,
        "divide" to BuiltIns::__divide,
        "equal" to BuiltIns::__equal,
        "nonequal" to BuiltIns::__nonequal,
        "less" to BuiltIns::__less,
        "lesseq" to BuiltIns::__lesseq,
        "greater" to BuiltIns::__greater,
        "greatereq" to BuiltIns::__greatereq,
        "and" to BuiltIns::__and,
        "or" to BuiltIns::__or,
        "not" to BuiltIns::__not,
        "read" to BuiltIns::__read,

        "setq" to BuiltIns::__setq,
        "retrun" to BuiltIns::__return,
        "read" to BuiltIns::__read
    )
    override fun call(treeNode: TreeNode, context: Context, opcodeList: OpcodeList){
        functions[(treeNode.childNodes[0] as AtomTreeNode).getValue()]?.let { it(treeNode, context, opcodeList) }
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
        val atomName = (treeNode.childNodes[1] as AtomTreeNode).getValue()
        val address = context.getAtomAddr(atomName)
        VirtualStackHelper.storeAtomValue(opcodeList, address.first)
    }

    private fun __return(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        if(context.isProg){
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

