package org.example.Generator
import org.example.ast.TreeNode
import org.example.Generator.Context
import org.example.Generator.CodeGenerator

class SpecialForm(private var address_length: Int) {
    private val specialForm: Map<String,
                (TreeNode, Context, OpcodeList, CodeGenerator) -> Unit> = mapOf(
                    "cond" to ::__cond,
                    "while" to ::__while,
                    "break" to ::__break,
                 )

    private var while_count: Int = 0
    private var current_while_id: Int = 0

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
        opcodeList.list[jump_from_check_to_true].__extraValue = dec_to_hex(block_id, 2 * self.__address_length)
        codeGenerator.process_call(treeNode.childNodes[2], context, opcodeList)
        opcodeList.add("PUSH")
        val jump_from_true_to_end = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        block_id = opcodeList.list.last().id
        opcodeList.list[jump_from_check_to_false].__extraValue = dec_to_hex(block_id, 2 * self.__address_length)
        if (treeNode.childNodes.size == 4) {
            codeGenerator.process_call(treeNode.childNodes[3], context, opcodeList)
        }
        opcodeList.add("PUSH")
        val jump_from_false_to_end = opcodeList.size() - 1
        opcodeList.add("JUMP")
        opcodeList.add("JUMPDEST")
        block_id = opcodeList.list.last().id
        opcodeList.list[jump_from_true_to_end].__extraValue = dec_to_hex(block_id, 2 * self.__address_length)
        opcodeList.list[jump_from_false_to_end].__extraValue = dec_to_hex(block_id, 2 * self.__address_length)
    }
    private fun __while(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        print("hi")
    }
    private fun __break(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        print("hi")
    }
}