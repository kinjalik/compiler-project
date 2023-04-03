package org.example.Semantic

import org.example.ast.AtomTreeNode
import org.example.ast.ListTreeNode
import org.example.ast.TreeNode

class SemanticAnalyzer(private var root: TreeNode) {

    private var prog: TreeNode? = null
    private var is_level: Int = 0
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
    private var wasReturn = false
    private var insideFunction = false

    private fun check_prog(current: TreeNode) {
        for (elem in current.childNodes) {
            when (elem) {
                is AtomTreeNode -> {
                    val atomTreeNode: AtomTreeNode = elem
                    if (atomTreeNode.getValue() == "prog") {
                        if (prog == null) {
                            prog = atomTreeNode
                            if (is_level != 1) {
                                throw Exception("Unexpected token \"prog\" on the current program level")
                            }
                        } else {
                            throw Exception("Token \"prog\" met more than once")
                        }
                    }
                }
            }
            is_level += 1
            check_prog(elem)
            is_level -= 1
        }
    }

    public fun check_prog() {
        check_prog(root)
        if (prog == null) {
            throw Exception("Token \"prog\" not found")
        }
    }

    private fun find_func(name: String, argc: Int): Boolean {
        var count = 0
        for (i in 0 until root.childNodes.size) {
            for (j in 0 until root.childNodes[i].childNodes.size) {
                if (root.childNodes[i].childNodes[j] is AtomTreeNode) {
                    val atomTreeNode = root.childNodes[i].childNodes[j] as AtomTreeNode
                    if (atomTreeNode.getValue() == "func" && j + 2 < root.childNodes[i].childNodes.size) {
                        if (root.childNodes[i].childNodes[j + 1] !is AtomTreeNode) {
                            continue
                        }
                        if (root.childNodes[i].childNodes[j + 2] !is ListTreeNode) {
                            continue
                        }
                        if ((root.childNodes[i].childNodes[j + 1] as AtomTreeNode).getValue() != name) {
                            continue
                        }
                        if (root.childNodes[i].childNodes[j + 2].childNodes.size != argc) {
                            continue
                        }
                        count += 1
                    }
                }
            }
        }
        if (count == 0) {
            return false
        } else if (count == 1) {
            return true
        } else {
            throw Exception("Multiple declarations of function \"$name\" found")
        }
    }

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

    private fun booleanToInt(b: Boolean) = if (b) 1 else 0
    private fun traverse_atom(ii: Int, current: TreeNode): Pair<Int, Boolean> {
        var i = ii

        val atomTreeNode = current.childNodes[i] as AtomTreeNode
        var code = 0

        var is_new_stack = false

        if (listOf("func", "prog", "while", "cond").contains(atomTreeNode.getValue())) {
            StackVar.new_stack()
            is_new_stack = true
        }

        if (atomTreeNode.getValue() == "setq") {
            StackVar.add_elem((current.childNodes[i + 1] as AtomTreeNode).getValue())
            i += 1
            return Pair(i, is_new_stack)
        }

        if (atomTreeNode.getValue() == "func") {
            val variables = current.childNodes[i + 2] as ListTreeNode
            for (elem in variables.childNodes) {
                StackVar.add_elem((elem as AtomTreeNode).getValue())
            }
            if (current.childNodes.size == 4) {
                i += 3
            }
            return Pair(i, is_new_stack)
        }

        if (atomTreeNode.getValue() == "prog") {
            return Pair(i, is_new_stack)
        }

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

        if (i == 0 && find_func(
                atomTreeNode.getValue(),
                current.childNodes.size - 1
            )
        ) {
            code += 1
        }

        code += booleanToInt(StackVar.contain(atomTreeNode.getValue()))

        if (code == 0) {
            throw Exception("Object with name \"${atomTreeNode.getValue()}\" do not exist")
        }

        if (code > 1) {
            throw Exception("Object with name \"${atomTreeNode.getValue()}\" has more than one definition")
        }

        return Pair(i, is_new_stack)
    }

    private fun traverse_atoms(current: TreeNode) {
        var i = 0
        var is_new_stack = false

        while (i < current.childNodes.size) {
            when (current.childNodes[i]) {
                is AtomTreeNode -> {
                    val returned = traverse_atom(i, current)
                    i = returned.first
                    is_new_stack = returned.second
                }
            }
            traverse_atoms(current.childNodes[i])
            if (is_new_stack) {
                StackVar.remove_last_stack()
                is_new_stack = false
            }
            i += 1
        }

    }

    fun traverse_atoms() {
        StackVar.new_stack()
        check_declarations(root)
        traverse_atoms(root)
        StackVar.remove_last_stack()
    }

    fun run() {
        StackVar.new_stack()

        check_prog(root)
        check_declarations(root)
        traverse_atoms(root)

        StackVar.remove_last_stack()
    }
}