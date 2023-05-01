package org.example.Generator
import org.example.Functions.BuiltIns
import org.example.ast.TreeNode
import org.example.Utils.Utils
import org.example.ast.AtomTreeNode

object SpecialForm {
    private val specialForm: Map<String,
                (TreeNode, Context, OpcodeList) -> Unit> = mapOf(
                    "cond" to ::__cond,
                    "while" to ::__while,
                    "break" to ::__break,
                 )

    private var while_count: Int = 0
    private var current_while_id: Int = 0
    private var address_length: Int = 0
    fun init(address_length: Int) {
        this.address_length = address_length
    }

    fun has(name: String): Boolean{
        return specialForm.contains(name)
    }

    fun call(treeNode: TreeNode, context: Context, opcodeList: OpcodeList){
        specialForm[(treeNode.childNodes[0] as AtomTreeNode).getValue()]?.let { it(treeNode, context, opcodeList) }
    }

    private fun __cond(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        CodeGenerator.process_call(treeNode.childNodes[1], context, opcodeList)
        opcodeList.add("PUSH")
        val jumpFromCheckToTrue = opcodeList.size() - 1
        opcodeList.add("JUMPI")
        opcodeList.add("PUSH")
        val jumpFromCheckToFalse = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        var blockId = opcodeList.list.last().id
        opcodeList.list[jumpFromCheckToTrue].__extraValue = Utils.decToHex(blockId, 2 * address_length)
        CodeGenerator.process_call(treeNode.childNodes[2], context, opcodeList)
        opcodeList.add("PUSH")
        val jumpFromTrueToEnd = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        blockId = opcodeList.list.last().id
        opcodeList.list[jumpFromCheckToFalse].__extraValue = Utils.decToHex(blockId, 2 * address_length)
        if (treeNode.childNodes.size == 4) {
            CodeGenerator.process_call(treeNode.childNodes[3], context, opcodeList)
        }
        opcodeList.add("PUSH")
        val jumpFromFalseToEnd = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        blockId = opcodeList.list.last().id
        opcodeList.list[jumpFromTrueToEnd].__extraValue = Utils.decToHex(blockId, 2 * address_length)
        opcodeList.list[jumpFromFalseToEnd].__extraValue = Utils.decToHex(blockId, 2 * address_length)
    }
    private fun __while(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        val prev_while = current_while_id
        current_while_id = while_count
        while_count += 1
        opcodeList.add("JUMPDEST", Utils.decToHex(current_while_id, 2 * address_length))
        val jumpdestToConditionCheckId = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)
        CodeGenerator.process_call(treeNode.childNodes[1], context, opcodeList)
        opcodeList.add("PUSH")
        val jumpToWhileTreeNode = opcodeList.size() - 1
        opcodeList.add("JUMPI")
        opcodeList.add("PUSH")
        val jumpToWhileEnd = opcodeList.size() - 1
        opcodeList.add("JUMP")

        opcodeList.add("JUMPDEST")
        opcodeList.list[jumpToWhileTreeNode].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)
        CodeGenerator.process_call(treeNode.childNodes[2], context, opcodeList)
        opcodeList.add("PUSH", jumpdestToConditionCheckId)
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        opcodeList.list[jumpToWhileEnd].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)

        for (i in 0 until opcodeList.size()) {
            if (opcodeList.list[i].__name == "JUMP" && opcodeList.list[i].__extraValue == Utils.decToHex(
                current_while_id,
                2 * address_length
            )) {
                opcodeList.list[i - 1].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)
                opcodeList.list[i].__extraValue = null
            }
        }

        BuiltIns.current_while = prev_while
    }
    private fun __break(treeNode: TreeNode, context: Context, opcodeList: OpcodeList) {
        opcodeList.add("PUSH", Utils.decToHex(0, 2 * address_length))
        opcodeList.add("JUMP", Utils.decToHex(current_while_id, 2 * address_length))
    }
}