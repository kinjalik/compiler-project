package org.example.Generator
import org.example.ast.TreeNode
import org.example.Generator.Context
import org.example.Generator.CodeGenerator
import org.example.Utils.Utils

object SpecialForm {
    private val specialForm: Map<String,
                (TreeNode, Context, OpcodeList, CodeGenerator) -> Unit> = mapOf(
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

    private fun __cond(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        codeGenerator.process_call(treeNode.childNodes[1], context, opcodeList)
        opcodeList.add("PUSH")
        val jump_from_check_to_true = opcodeList.size() - 1
        opcodeList.add("JUMPI")
        opcodeList.add("PUSH")
        val jump_from_check_to_false = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        var block_id = opcodeList.list.last().id
        opcodeList.list[jump_from_check_to_true].__extraValue = Utils.decToHex(block_id, 2 * address_length)
        codeGenerator.process_call(treeNode.childNodes[2], context, opcodeList)
        opcodeList.add("PUSH")
        val jump_from_true_to_end = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        block_id = opcodeList.list.last().id
        opcodeList.list[jump_from_check_to_false].__extraValue = Utils.decToHex(block_id, 2 * address_length)
        if (treeNode.childNodes.size == 4) {
            codeGenerator.process_call(treeNode.childNodes[3], context, opcodeList)
        }
        opcodeList.add("PUSH")
        val jump_from_false_to_end = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        block_id = opcodeList.list.last().id
        opcodeList.list[jump_from_true_to_end].__extraValue = Utils.decToHex(block_id, 2 * address_length)
        opcodeList.list[jump_from_false_to_end].__extraValue = Utils.decToHex(block_id, 2 * address_length)
    }
    private fun __while(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        val prev_while = current_while_id
        current_while_id = while_count
        while_count += 1
        opcodeList.add("JUMPDEST", Utils.decToHex(current_while_id, 2 * address_length))
        val jumpdest_to_condition_check_id = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)
        codeGenerator.process_call(treeNode.childNodes[1], context, opcodeList)
        opcodeList.add("PUSH")
        val jump_to_while_treeNode = opcodeList.size() - 1
        opcodeList.add("JUMPI")
        opcodeList.add("PUSH")
        val jump_to_while_end = opcodeList.size() - 1
        opcodeList.add("JUMP")

        opcodeList.add("JUMPDEST")
        opcodeList.list[jump_to_while_treeNode].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)
        codeGenerator.process_call(treeNode.childNodes[2], context, opcodeList)
        opcodeList.add("PUSH", jumpdest_to_condition_check_id)
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        opcodeList.list[jump_to_while_end].__extraValue = Utils.decToHex(opcodeList.list.last().id, 2 * address_length)

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
    private fun __break(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        opcodeList.add("PUSH", Utils.decToHex(0, 2 * address_length))
        opcodeList.add("JUMP", Utils.decToHex(current_while_id, 2 * address_length))
    }
}