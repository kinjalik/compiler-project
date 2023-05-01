package org.example.ast

import org.example.exceptions.TreeBuildException
import org.example.tokens.AtomToken
import org.example.tokens.PhantomToken
import org.example.tokens.Token

class AtomTreeNode : AccessibleTreeNode<String>() {
    override var value: String = ""
    override fun parse(token: Token, tokenIter: Iterator<Token>): TreeNode {
        var tk = token
        while (tk is PhantomToken)
            tk = tokenIter.next()
        if (tk !is AtomToken)
            throw TreeBuildException(String.format("Unexpected token while parsing Element: %s",
                tk.javaClass.kotlin.simpleName))

        value = tk.value
        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Atom \"%s\"\n", " ".repeat(PRINT_INTENT * depth), value))
        super.print(depth + 1)
    }
}