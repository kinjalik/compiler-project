package org.example.CFG

import org.example.WarningCollection.WarningCollection
import org.example.ast.AtomTreeNode
import org.example.ast.ListTreeNode
import org.example.ast.TreeNode
import org.example.exceptions.CFGException
import java.util.*

object BuiltIn {
    val buildIn = setOf(
        "plus",
        "minus",
        "times",
        "divide",

        "equal",
        "nonequal",
        "less",
        "lesseq",
        "greater",
        "greatereq",

        "and",
        "or",
        "not",

        "read",

        "setq",

        "return",

        "cond",
        "while",
        "break",
    )
}

class CFG(
    private val treeNode: TreeNode
) {
    var functions = ArrayList<FunctionNode>()
    fun run() {
        for (el in treeNode.childNodes) {
            if ((el.childNodes[0] as AtomTreeNode).getValue() == "func") {
                functions.add(FunctionNode(functionName = (el.childNodes[1] as AtomTreeNode).getValue()))
            } else {
                functions.add(FunctionNode())
            }
        }

        for (i in 0 until treeNode.childNodes.size) {
            val el = treeNode.childNodes[i]
            if ((el.childNodes[0] as AtomTreeNode).getValue() == "func") {
                val variableArray: ArrayList<String> = arrayListOf()
                for (j in el.childNodes[2].childNodes) {
                    variableArray.add((j as AtomTreeNode).getValue())
                }

                functions[i].cfg = CFGNode(el.childNodes[3], 0, allFunction=functions).run()
                functions[i].functionName = (el.childNodes[1] as AtomTreeNode).getValue()
                functions[i].variablePass = variableArray
            } else if ((el.childNodes[0] as AtomTreeNode).getValue() == "prog") {
                functions[i].cfg = CFGNode(el.childNodes[1], 0, allFunction=functions).run()
            }
        }
    }

    fun print() {
        for (el in functions) {
            println("__________________________________")
            println("\u001B[36m${el.functionName}")
            if (el.variablePass.size > 0) {
                println("\tVariable pass: ${el.variablePass}")
            }
            println()

            el.cfg?.print()
            println("__________________________________")
        }
    }
}

data class FunctionNode(
    var cfg: CFGNode? = null,
    var functionName: String = "prog",
    var variablePass: ArrayList<String> = arrayListOf(),
)

class CFGNode(
    val partNode: TreeNode,
    val it: Int,
    var toContinue: CFGNode? = null,
    val allFunction: ArrayList<FunctionNode> = ArrayList<FunctionNode>(),
    val prevNode: CFGNode? = null,
    var isList: Boolean = false,
    var iAmWarning: Boolean = false,
) {
    var toFalse: CFGNode? = null
    var toTrue: CFGNode? = null
    var toStraight: CFGNode? = null
    var variableWrite: String? = null
    var variableRead = TreeSet<String>()
    var callFunction = ArrayList<FunctionNode>()
    var prevNodes = ArrayList<CFGNode>()
    var toWarning: CFGNode? = null
    var isReturn = false

    var context: String = ""

    private fun parseVariable(nodeWithVariable: TreeNode) {
        if (nodeWithVariable is ListTreeNode) {
            for (i in nodeWithVariable.childNodes) {
                parseVariable(i)
            }
        } else if (nodeWithVariable is AtomTreeNode) {
            val name = nodeWithVariable.getValue()
            if (BuiltIn.buildIn.contains(name)) {
                return
            }

            val functionNode = allFunction.find {it.functionName == name }
            if (functionNode != null) {
                callFunction.add(functionNode)
                return
            }

            variableRead.add(name)
        }
    }


    fun run(): CFGNode? {

        if (prevNode != null) {
            prevNodes.add(prevNode)
        }
        if (it < partNode.childNodes.size) {
            val el = partNode.childNodes[it]
            if (el.childNodes.isEmpty()) {
                return this
            }
            if (el.childNodes[0] is ListTreeNode) {
                return CFGNode(el, 0, toContinue, allFunction=allFunction, prevNode, true,
                    iAmWarning=iAmWarning).run()
            }
            val isIt = (el.childNodes[0] as AtomTreeNode).getValue()
            context = isIt

            if (iAmWarning) {
                WarningCollection.addWarning("${el.childNodes[0].numberLine}:${el.childNodes[0].numberCh}:${context} is unreachable")
//                println("I am warning $context")
            }

            for (i in 1 until el.childNodes.size) {
                if (isIt == "setq" && i == 1) {
                    continue
                }
                if ((isIt == "cond" || isIt == "while") && i != 1) {
                    continue
                }
                parseVariable(el.childNodes[i])
            }

            when (isIt) {
                "cond" -> {
                    toContinue = CFGNode(partNode, it + 1, toContinue,allFunction=allFunction, null,
                        iAmWarning=iAmWarning).run()

                    toTrue = CFGNode(el, 2, toContinue, allFunction=allFunction, this,
                        iAmWarning=iAmWarning).run()
                    toFalse = if (el.childNodes.size > 3) {
                        CFGNode(el, 3, toContinue,allFunction=allFunction, this,
                            iAmWarning=iAmWarning).run()
                    } else {
                        toContinue
                    }
                }

                "while" -> {
                    toFalse = CFGNode(partNode, it + 1, toContinue, allFunction=allFunction, this,
                        iAmWarning=iAmWarning).run()
                    toTrue = CFGNode(el.childNodes[2], 0, this, allFunction=allFunction, this,
                        iAmWarning=iAmWarning).run()
                }

                "break" -> {
//                    toStraight = toContinue?.toFalse
//                    toStraight?.prevNodes?.add(this)
                    var temp = prevNode
                    while (temp?.context != "while") {
                        temp = temp?.prevNode
                        if (temp == null) {
                            throw CFGException("\"break\" without \"while\"")
                        }
                    }
                    toStraight = temp.toFalse
                    toStraight?.prevNodes?.add(this)

                    toWarning = CFGNode(partNode, it + 1, toContinue,allFunction=allFunction, null, iAmWarning=true).run()
                }

                "return" -> {
                    isReturn = true
                }

                else -> {
                    if (isIt == "setq") {
                        variableWrite = (el.childNodes[1] as AtomTreeNode).getValue()
                    }
                    if ((prevNode?.context ?: "") != "cond" || isList) {
                        toStraight = CFGNode(partNode, it + 1, toContinue, allFunction=allFunction, this, true,
                            iAmWarning=iAmWarning).run()
                    } else {
                        toStraight = toContinue
//                        if (toStraight?.context != "while") { unrechable
                        toStraight?.prevNodes?.add(this)
//                        }

                    }
                }
            }
        } else {
            this.prevNode?.let { it1 -> toContinue?.prevNodes?.add(it1) }
            return toContinue
        }
        return this
    }

    companion object {
        var was: MutableSet<CFGNode> = mutableSetOf()
    }

    fun print(pad: Int = 0) {
        if (was.contains(this)) {
            (0..pad).forEach { _ -> print("-") }
            println("End branch")
            return
        }
        was.add(this)

        (0..pad).forEach { _ -> print("-") }
        print("\u001B[33mI am: ${this.context}\n")
        if (variableWrite != null) {
            print("\tvariable write: ${variableWrite}\n")
        }
        if (variableRead.size > 0) {
            print("\tvariable read: ${variableRead}\n")
        }
        if (callFunction.size > 0) {
            val arr: ArrayList<String> = ArrayList()
            callFunction.forEach { arr.add(it.functionName) }
            print("\tfunctions call: ${arr}\n")
        }
        if (prevNodes.size > 0) {
            print("\tprev nodes:")
            prevNodes.forEach{ print(" ${it.context}") }
            println()
        }

        var i = 0
        val iNames = arrayOf("To true", "To False", "To Straight", "To Warning")
        val iColor = arrayOf("\u001B[32m", "\u001b[31m", "\u001B[34m", "\u001B[35m")
        arrayOf(toTrue, toFalse, toStraight, toWarning).forEach {
            if (it != null) {
                (0..pad).forEach { _ -> print("-") }
                print("${iColor[i]}${iNames[i]}: ${it.context}\n")

                if (it.variableWrite != null) {
                    print("\tvariable write: ${it.variableWrite}\n")
                }

                if (it != toStraight || it.context != "break") {

                    if (i != 2) {
                        it.print(pad + 1)
                    } else {
                        it.print(pad)
                    }
                }
            }
            ++i
        }
    }
}