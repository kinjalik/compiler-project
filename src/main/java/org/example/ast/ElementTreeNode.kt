package org.example.ast

import org.example.exceptions.TreeBuildException
import org.example.tokens.AtomToken
import org.example.tokens.DigitToken
import org.example.tokens.ParenthesisToken
import org.example.tokens.PhantomToken
import org.example.tokens.Token

class ElementTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Iterator<Token>): TreeNode {
        var nextTk = token
        while (nextTk is PhantomToken)
            nextTk = tokenIter.next()

        val childNode = when (nextTk) {
            is AtomToken -> AtomTreeNode().parse(nextTk, tokenIter)
            is DigitToken -> LiteralTreeNode().parse(nextTk, tokenIter)
            is ParenthesisToken -> ListTreeNode().parse(nextTk, tokenIter)
            else -> throw TreeBuildException(
                String.format("Unexpected token while parsing Element: %s", nextTk.javaClass.kotlin.simpleName)
            )
        }
        return childNode
    }

    override fun print(depth: Int) {
//        print(String.format("%s- Element\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth)
    }
}