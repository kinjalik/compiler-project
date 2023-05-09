package org.example.ast

import org.example.exceptions.TreeBuildException
import org.example.tokens.*

class ElementTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Scanner): TreeNode {
        var nextTk = token
        while (nextTk is PhantomToken)
            nextTk = tokenIter.next()

        val childNode = when (nextTk) {
            is AtomToken -> AtomTreeNode().parse(nextTk, tokenIter)
            is DigitToken -> LiteralTreeNode().parse(nextTk, tokenIter)
            is ParenthesisToken -> ListTreeNode().parse(nextTk, tokenIter)
            else -> throw TreeBuildException(
                tokenIter.getCurrentLine(),
                tokenIter.getCurrentCol(),
                nextTk.toString())
        }
        return childNode
    }

    override fun print(depth: Int) {
//        print(String.format("%s- Element\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth)
    }
}