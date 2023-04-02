package org.example.Semantic

import org.example.ast.AtomTreeNode
import org.example.ast.ListTreeNode
import org.example.ast.TreeNode

class SemanticAnalyzer(private var root: TreeNode) {
    private val predfined: Map<String, Int> = mapOf(
        "plus" to 2,
        "minus" to 2,
        "times" to 2,
        "divide" to 2,

        "equal" to 2,
        "nonequal" to 2,
        "less" to 2,
        "lesseq" to 2,
        "greater" to 2,
        "greatereq" to 2,

        "and" to 2,
        "or" to 2,
        "not" to 1,

        "read" to 1,

        "setq" to 2,

        "return" to 1,

        "cond" to -1,
        "while" to 2,
    )

    private fun check_declarations(current: TreeNode) {
        for (i in 0 until current.childNodes.size) {
            when (current.childNodes[i]) {
                is AtomTreeNode -> {
                    var checked: AtomTreeNode = current.childNodes[i] as AtomTreeNode
                    if (checked.getValue() == "func") {
                        if (i != 0) {
                            throw Exception("func declaration keyword should be first in the List")
                        }

                        if (current.childNodes.size != 4) {
                            if (current.childNodes.size < 2) {
                                throw Exception("Invalid function declaration found")
                            } else if (current.childNodes[i + 1] is AtomTreeNode) {
                                throw Exception("Invalid declaration of function \"" + (current.childNodes[i + 1] as AtomTreeNode).getValue() + "\" found")
                            } else {
                                throw Exception("Unknown error in function declaration, note: \n\t func <name> <List args> <List body> is the only correct form")
                            }
                        }

                    } else if (checked.getValue() == "setq") {
                        if (i != 0) {
                            throw Exception("Setq operation should be first in the List")
                        }
                        if (current.childNodes.size != 3) {
                            if (current.childNodes.size < 2) {
                                throw Exception("Invalid variable set operation")
                            } else if (current.childNodes[i + 1] is AtomTreeNode) {
                                throw Exception("Invalid usage of setq for variable  \"" + (current.childNodes[i + 1] as AtomTreeNode).getValue() + "\" found")
                            } else {
                                throw Exception("Unknown error in variable usage setq, note: \n\t setq <name> <value List/Atom/Literal> is the only correct form")
                            }
                        }
                    }
                }
            }
            check_declarations(current.childNodes[i])
        }
    }

    private fun traverse_atom(ii: Int, current: TreeNode){
        var i = ii

        val atomTreeNode = current.childNodes[i] as AtomTreeNode
        var code = 0

        if (predfined.contains(atomTreeNode.getValue())) {
            val size: Int =
                predfined.getOrDefault(atomTreeNode.getValue(), 0)

            if (atomTreeNode.getValue() == "cond" && i == 0 &&
                (current.childNodes.size - 1 != 2 && current.childNodes.size - 1 != 3)
            ) {
                throw Exception(
                    "Call of \"cond\" requires 2 or 3 arguments " +
                            "but ${current.childNodes.size - 1} were provided"
                )
            }

            val not_cond = atomTreeNode.getValue() != "cond"

            if (i == 0 && current.childNodes.size - 1 != size && not_cond) {
                throw Exception(
                    "Call of \"${atomTreeNode.getValue()}\" requires $size arguments " +
                            "but ${current.childNodes.size - 1} were provided"
                )
            }

            if (i != 0 && not_cond) {
                throw Exception(
                    "Function \"${atomTreeNode.getValue()}\" " +
                            "should be the first element of the list"
                )
            }

            code += 1
        }

        if (code == 0) {
            throw Exception("Object with name \"${atomTreeNode.getValue()}\" do not exist")
        }

        if (code > 1) {
            throw Exception("Object with name \"${atomTreeNode.getValue()}\" has more than one definition")
        }
    }

    private fun traverse_atoms(current: TreeNode) {
        var i = 0
        while (i < current.childNodes.size) {
            when (current.childNodes[i]) {
                is AtomTreeNode -> {
                    traverse_atom(i, current)
                }
            }
            traverse_atoms(current.childNodes[i])
            //TODO Stack
        }
    }

    fun run() {
        check_declarations(root)
        traverse_atoms(root)
    }

}
