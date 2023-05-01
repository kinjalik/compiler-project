package org.example.ast

import org.example.exceptions.TreeBuildException
import org.example.tokens.DigitToken
import org.example.tokens.PhantomToken
import org.example.tokens.Token

class LiteralTreeNode : AccessibleTreeNode<Int>() {
    override var value: Int = 0
    override fun parse(token: Token, tokenIter: Iterator<Token>): TreeNode {
        var tk = token
        while (tk is PhantomToken)
            tk = tokenIter.next()

        if (tk !is DigitToken)
            throw TreeBuildException(String.format("Unexpected token while parsing Literal: %s",
                tk.javaClass.kotlin.simpleName))

        value = tk.value
        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Literal \"%d\"\n", " ".repeat(PRINT_INTENT * depth), value))
        super.print(depth + 1)
    }
}