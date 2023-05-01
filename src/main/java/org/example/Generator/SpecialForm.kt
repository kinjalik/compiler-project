package org.example.Generator
import org.example.ast.TreeNode
import org.example.Generator.Context
import org.example.Generator.CodeGenerator

class SpecialForm {
    private val specialForm: Map<String,
                (TreeNode, Context, OpcodeList, CodeGenerator) -> Unit> = mapOf(
                    "cond" to ::__cond,
                 )

    private fun __cond(treeNode: TreeNode, context: Context, opcodeList: OpcodeList, codeGenerator: CodeGenerator) {
        print("hi")
    }
}