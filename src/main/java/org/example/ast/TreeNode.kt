package org.example.ast

import org.example.tokens.Scanner
import org.example.tokens.Token

abstract class TreeNode {
    protected val PRINT_INTENT = 1
    var childNodes: List<TreeNode> = mutableListOf()
        protected set
    abstract fun parse(token: Token, tokenIter: Scanner): TreeNode

    var numberLine: Int = 0
    var numberCh: Int = 0
    open fun print(depth: Int = 0): Unit = childNodes.forEach { it.print(depth + 1) }
}